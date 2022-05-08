import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.util.HashMap;

public class Dispatcher implements Runnable {
    private final LockArrayList<Request> queue; // total queue
    private final ElevatorQueue<Elevator> elevators = new ElevatorQueue<>();
    // queue is actually a map, means the same building/floor, the number of the available elevators
    private final HashMap<Integer, Integer> counter = new HashMap<>();
    private final HashMap<Integer, Integer> switchArrangement = new HashMap<>();
    
    public Dispatcher(LockArrayList<Request> queue) {
        this.queue = queue;
        
        VerticalElevator elevatorA = new VerticalElevator(1, 'A', 1, 10,
                1023, 0.6, 8, queue);
        new Thread(elevatorA, "A-1").start();
        elevators.add('A', elevatorA);
        counter.put((int) 'A', 0);
        VerticalElevator elevatorB = new VerticalElevator(2, 'B', 1, 10,
                1023, 0.6, 8, queue);
        new Thread(elevatorB, "B-2").start();
        elevators.add('B', elevatorB);
        counter.put((int) 'B', 0);
        VerticalElevator elevatorC = new VerticalElevator(3, 'C', 1, 10,
                1023, 0.6, 8, queue);
        new Thread(elevatorC, "C-3").start();
        elevators.add('C', elevatorC);
        counter.put((int) 'C', 0);
        VerticalElevator elevatorD = new VerticalElevator(4, 'D', 1, 10,
                1023, 0.6, 8, queue);
        new Thread(elevatorD, "D-4").start();
        elevators.add('D', elevatorD);
        counter.put((int) 'D', 0);
        VerticalElevator elevatorE = new VerticalElevator(5, 'E', 1, 10,
                1023, 0.6, 8, queue);
        new Thread(elevatorE, "E-5").start();
        elevators.add('E', elevatorE);
        counter.put((int) 'E', 0);
        HorizontalElevator elevator = new HorizontalElevator(6, 'A', 1, 5,
                31, 0.6, 8, queue);
        elevators.add(10031, elevator);
        new Thread(elevator, "1fl-6").start();
        counter.put(10031, 0);
    }
    
    @Override
    public void run() {
        while (true) {
            queue.lock();
            try {
                if (queue.isEmpty()) {
                    if (!queue.isStop() || !Count.getInstance().isCleared()) {
                        queue.await();
                    }
                    if (queue.isStop() && queue.isEmpty() && Count.getInstance().isCleared()) {
                        setStop();
                        break;
                    }
                } else {
                    Request request = queue.pop();
                    if (request instanceof ElevatorRequest) {
                        dealWithElevator(request);
                    } else {
                        PersonRequest personRequest = (PersonRequest) request;
                        MyRequest myRequest = new MyRequest(personRequest);
                        //System.out.println(myRequest);
                        char building = myRequest.getFromBuilding();
                        if (myRequest.getFromBuilding() == myRequest.getToBuilding()) {
                            chooseElevator(building, counter.get((int) building), myRequest);
                        } else {
                            int transparent = transparentFloor(myRequest);
                            int transparentFloor = transparent / 10000;
                            //System.out.println(transparent);
                            if (transparentFloor != myRequest.getFromFloor()) {
                                myRequest.setFrom(myRequest.getFromFloor());
                                myRequest.setDestination(transparentFloor);
                                chooseElevator(building, counter.get((int) building), myRequest);
                            } else {
                                chooseElevator(transparent, counter.get(transparent)
                                        , myRequest);
                            }
                        }
                    }
                }
            } finally {
                queue.unlock();
            }
        }
        //System.out.println("Dispatcher Over");
    }
    
    private void setStop() {
        for (int i = 1; i <= 10; ++i) {
            for (int k = 1; k < 32; ++k) {
                int key = i * 10000 + k;
                if (!elevators.containsKey(key)) {
                    continue;
                }
                for (int j = 0; j < elevators.get(key).size(); ++j) {
                    elevators.get(key).get(j).setStop();
                    //elevators.get(i).get(j).queueSignal();
                }
            }
        }
        
        for (int i = 'A'; i <= 'E'; ++i) {
            for (int j = 0; j < elevators.get(i).size(); ++j) {
                elevators.get(i).get(j).setStop();
                //elevators.get(i).get(j).queueSignal();
            }
        }
    }
    
    private void chooseElevator(int index, int num, MyRequest myRequest) {
        elevators.lock();
        try {
            //System.out.println(elevators.get(index).get(num).getId());
            elevators.get(index).get(num).addQueue(myRequest);
            elevators.get(index).get(num).addOut(myRequest);
            int count = (num + 1) % elevators.get(index).size();
            counter.put(index, count);
        } finally {
            elevators.unlock();
        }
    }
    
    private boolean buildingAvailable(int switchInfo, int from, int to) {
        return ((switchInfo >> from) & 1) + ((switchInfo >> to) & 1) == 2;
    }
    
    private int goThrough(int i, MyRequest myRequest) {
        int key = i * 10000 + (myRequest.getFromBuilding()) * 100 + (myRequest.getToBuilding());
        boolean flag = false;
        /* true means find the only one (no other matches)
         * false means there are no available
         */
        for (int j = 1; j < 32; ++j) {
            if (elevators.containsKey(i * 10000 + j)
                    && buildingAvailable(j,
                    myRequest.getFromBuilding() - 'A', myRequest.getToBuilding() - 'A')) {
                if (!switchArrangement.containsKey(key)) {
                    switchArrangement.put(key, j);
                    return j;
                } else {
                    flag = true;
                    if (j != switchArrangement.get(key)) {
                        switchArrangement.put(key, j);
                        return j;
                    }
                }
            }
        }
        if (flag) {
            return switchArrangement.get(key);
        }
        return 0;
    }
    
    private int transparentFloor(MyRequest myRequest) {
        int fromFloor = myRequest.getFromFloor();
        int toFloor = myRequest.getToFloor();
        for (int i = fromFloor; i <= toFloor; ++i) {
            int info = goThrough(i, myRequest);
            if (info != 0) {
                return i * 10000 + info;
            }
        }
        for (int i = 1; i <= 10; ++i) {
            int info = goThrough(toFloor + i, myRequest);
            if (toFloor + i <= 10 && info != 0) {
                return (toFloor + i) * 10000 + info;
            }
            info = goThrough(fromFloor - i, myRequest);
            if (fromFloor - i >= 1 && info != 0) {
                return (fromFloor - i) * 10000 + info;
            }
        }
        return 1;
    }
    
    private void dealWithElevator(Request request) {
        ElevatorRequest elevatorRequest = (ElevatorRequest) request;
        if (elevatorRequest.getType().equals("building")) {
            int id = elevatorRequest.getElevatorId();
            Character building = elevatorRequest.getBuilding();
            VerticalElevator elevator = new VerticalElevator(id, building, 1, 10,
                    1023, elevatorRequest.getSpeed()
                    , elevatorRequest.getCapacity(), queue);
            elevators.lock();
            try {
                elevators.add(building, elevator);
            } finally {
                elevators.unlock();
            }
            new Thread(elevator, building + "-" + id).start();
            
        } else if (elevatorRequest.getType().equals("floor")) {
            int id = elevatorRequest.getElevatorId();
            int floor = elevatorRequest.getFloor();
            int switchInfo = elevatorRequest.getSwitchInfo();
            HorizontalElevator elevator = new HorizontalElevator(id, 'A', floor, 5,
                    switchInfo, elevatorRequest.getSpeed(),
                    elevatorRequest.getCapacity(), queue);
            elevators.lock();
            try {
                elevators.add(floor * 10000 + switchInfo, elevator);
            } finally {
                elevators.unlock();
            }
            new Thread(elevator, floor + "fl-" + id).start();
            int key = floor * 10000 + switchInfo;
            if (!counter.containsKey(key)) {
                counter.put(key, 0);
            }
        }
    }
}
