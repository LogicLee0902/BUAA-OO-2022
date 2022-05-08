import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.util.HashMap;

public class Dispatcher implements Runnable {
    private final LockArrayList<Request> queue; // total queue
    private final ElevatorQueue<Elevator> elevators = new ElevatorQueue<>();
    // queue is actually a map, means the same building/floor, the number of the available elevators
    private final HashMap<Integer, Integer> counter = new HashMap<>();
    
    public Dispatcher(LockArrayList<Request> queue) {
        this.queue = queue;
        
        VerticalElevator elevatorA = new VerticalElevator(1, 'A', 1, 10);
        new Thread(elevatorA, "A-1").start();
        elevators.add('A', elevatorA);
        counter.put((int) 'A', 0);
        VerticalElevator elevatorB = new VerticalElevator(2, 'B', 1, 10);
        new Thread(elevatorB, "B-2").start();
        elevators.add('B', elevatorB);
        counter.put((int) 'B', 0);
        VerticalElevator elevatorC = new VerticalElevator(3, 'C', 1, 10);
        new Thread(elevatorC, "C-3").start();
        elevators.add('C', elevatorC);
        counter.put((int) 'C', 0);
        VerticalElevator elevatorD = new VerticalElevator(4, 'D', 1, 10);
        new Thread(elevatorD, "D-4").start();
        elevators.add('D', elevatorD);
        counter.put((int) 'D', 0);
        VerticalElevator elevatorE = new VerticalElevator(5, 'E', 1, 10);
        new Thread(elevatorE, "E-5").start();
        elevators.add('E', elevatorE);
        counter.put((int) 'E', 0);
    }
    
    @Override
    public void run() {
        while (true) {
            queue.lock();
            try {
                if (queue.isEmpty()) {
                    if (!queue.isStop()) {
                        queue.await();
                    }
                    if (queue.isStop() && queue.isEmpty()) {
                        setStop();
                        break;
                    }
                } else {
                    Request request = queue.pop();
                    if (request instanceof ElevatorRequest) {
                        ElevatorRequest elevatorRequest = (ElevatorRequest) request;
                        if (elevatorRequest.getType().equals("building")) {
                            int id = elevatorRequest.getElevatorId();
                            Character building = elevatorRequest.getBuilding();
                            VerticalElevator elevator = new VerticalElevator(id, building, 1, 10);
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
                            HorizontalElevator elevator = new HorizontalElevator(id, 'A', floor, 5);
                            elevators.lock();
                            try {
                                elevators.add(floor, elevator);
                            } finally {
                                elevators.unlock();
                            }
                            new Thread(elevator, floor + "fl-" + id).start();
                            if (!counter.containsKey(floor)) {
                                counter.put(floor, 0);
                            }
                        }
                    } else {
                        PersonRequest personRequest = (PersonRequest) request;
                        MyRequest myRequest = new MyRequest(personRequest);
                        if (myRequest.getFromBuilding() == myRequest.getToBuilding()) {
                            char building = myRequest.getFromBuilding();
                            chooseElevator(building, counter.get((int)building), myRequest);
                        } else {
                            int floor = myRequest.getFromFloor();
                            chooseElevator(floor, counter.get(floor), myRequest);
                        }
                    }
                }
            } finally {
                queue.unlock();
            }
        }
    }
    
    private void setStop() {
        for (int i = 1; i <= 10; ++i) {
            if (!elevators.containsKey(i)) {
                continue;
            }
            for (int j = 0; j < elevators.get(i).size(); ++j) {
                elevators.get(i).get(j).setStop();
                //elevators.get(i).get(j).queueSignal();
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
}
