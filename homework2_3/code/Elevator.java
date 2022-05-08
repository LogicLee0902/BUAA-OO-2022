import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.util.concurrent.locks.ReentrantLock;

// the common things between vertical & horizontal
public abstract class Elevator implements Runnable {
    private final Demand<MyRequest> inside;
    private final Demand<MyRequest> outside;
    private final LockArrayList<MyRequest> queue;
    private final int id;
    private MyRequest mainRequest;
    private Character building;
    private final LockArrayList<Request> requests;
    
    public void setDirection(int direction) {
        this.direction = direction;
    }
    
    public void setCurrent(int current) {
        this.current = current;
    }
    
    public int getCurrent() {
        return current;
    }
    
    public void setBuilding(Character building) {
        this.building = building;
    }
    
    public int getId() {
        return id;
    }
    
    private int direction;
    private int current;
    private int floor;
    private final int capacity;
    private int num;
    private final ReentrantLock locker = new ReentrantLock();
    // private final Condition condition = locker.newCondition();
    private final int high;
    private final int available;
    private final double speed;
    
    public Elevator(int id, Character building, int floor, int high, int available,
                    double speed, int capacity, LockArrayList<Request> requests) {
        inside = new Demand<>();
        outside = new Demand<>();
        this.queue = new LockArrayList<>();
        mainRequest = null;
        this.id = id;
        this.building = building;
        this.floor = floor;
        direction = 0;
        // for horizontal, 1 means A->B, -1 means A->E
        current = 1;
        this.capacity = capacity;
        this.speed = speed;
        num = 0;
        this.high = high;
        this.available = available;
        this.requests = requests;
    }
    
    public void queueSignal() {
        queue.lock();
        queue.signalAll();
    }
    
    public void addQueue(MyRequest request) {
        locker.lock();
        try {
            queue.lock();
            try {
                queue.add(request);
            } finally {
                queue.unlock();
            }
        } finally {
            locker.unlock();
        }
    }
    
    public void addOut(MyRequest request) {
        locker.lock();
        try {
            outside.add(request.getFrom(), request);
        } finally {
            locker.unlock();
        }
    }
    
    public void setStop() {
        locker.lock();
        try {
            queue.setStop();
        } finally {
            locker.lock();
        }
    }
    
    public abstract void changeDirection(int target);
    
    public void dealWithInside(int high) {
        boolean flag = false;
        for (MyRequest request : inside.get(current)) {
            if (request.equals(mainRequest)) {
                // the mainRequest has been done, need to select another one
                flag = true;
            }
            --num;
            OutputThread.getInstance().println("OUT-" + request.getPersonId() + "-"
                    + building + "-" + floor + "-" + id);
            request.update(high, current);
            if (request.isArrive()) {
                Count.getInstance().decrease();
                if (Count.getInstance().isCleared()) {
                    requests.lock();
                    try {
                        requests.signalAll();
                    } finally {
                        requests.unlock();
                    }
                }
            } else {
                PersonRequest personRequest = new PersonRequest(request.getTranFloor(),
                        request.getToFloor(), request.getTranBuilding(),
                        request.getToBuilding(), request.getPersonId());
                requests.lock();
                try {
                    requests.add(personRequest);
                    requests.signalAll();
                } finally {
                    requests.unlock();
                }
            }
        }
        if (flag) {
            mainRequest = null;
            direction = 0;
            selectMainRequest(high);
        }
        // all passengers arrived target floor
        inside.remove(current);
    }
    
    public boolean dealWithOutside(boolean open, int high) {
        boolean flag = open;
        for (MyRequest request : outside.get(current)) {
            // to see if there is someone can be taken with
            // or the main request is waiting
            if (num == capacity) {
                if (outside.get(current).contains(mainRequest)) {
                    // reach the max num, need to change the main request
                    mainRequest = null;
                    selectMainRequest(high);
                }
                break;
            }
            if (high == 5 || isCarry(request)
                    || (request.getDestination() < current && direction == -1)
                    || (request.equals(mainRequest) && inside.isEmpty()) || mainRequest == null) {
                if (!flag) {
                    // if the door is shut down, the open the door
                    OutputThread.getInstance().println(
                            "OPEN-" + building + "-" + floor + "-" + id);
                    sleep(200);
                    flag = true;
                }
                if (request.equals(mainRequest)) {
                    changeDirection(mainRequest.getDestination());
                }
                // outside becomes inside
                num++;
                OutputThread.getInstance().println(
                        "IN-" + request.getPersonId() + "-"
                                + building + "-" + floor + "-" + id);
                inside.add(request.getDestination(), request);
                // remove outside
                queue.lock();
                try {
                    queue.removeRequest(request);
                } finally {
                    queue.unlock();
                }
                outside.lock();
                try {
                    outside.get(current).remove(request);
                } finally {
                    outside.unlock();
                }
            } else if (request.equals(mainRequest) && (!inside.isEmpty())) {
                // means mainRequest's direction is opposite to the current direction
                // then we need to switch another mainRequest
                mainRequest = null;
                selectMainRequest(high);
            }
        }
        outside.lock();
        try {
            if (outside.get(current).isEmpty()) {
                outside.remove(current);
            }
        } finally {
            outside.unlock();
        }
        return flag;
    }
    
    public void selectMainRequest(int high) {
        /*
         * 1 ~ 10 means the elevator is vertical
         * 1 ~ 5 means the elevator is horizontal
         */
        if (mainRequest == null) {
            if (inside.isEmpty()) {
                queue.lock();
                try {
                    if (queue.isEmpty()) {
                        if (!queue.isStop()) {
                            queue.await();
                        }
                        if (queue.isStop() && queue.isEmpty()) {
                            return;
                        }
                    }
                    mainRequest = queue.first();
                    if (mainRequest != null) {
                        changeDirection(mainRequest.getFrom());
                    }
                } finally {
                    queue.unlock();
                }
            } else {
                for (int i = 1; i <= high; ++i) {
                    /* if there is someone in the elevator
                     * find a request whose target is nearest to the current floor
                     * whatever is above or below the current floor
                     */
                    if (current + i <= high && inside.containsKey(current + i)) {
                        mainRequest = inside.get(current + i).get(0);
                        changeDirection(mainRequest.getDestination());
                        break;
                    } else if (current - i >= 1 && inside.containsKey(current - i)) {
                        mainRequest = inside.get(current - i).get(0);
                        changeDirection(mainRequest.getDestination());
                        break;
                    }
                }
            }
        }
    }
    
    public abstract boolean move(int direction);
    
    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void run() {
        while (true) {
            queue.lock();
            try {
                if (queue.isEmpty() && queue.isStop() && inside.isEmpty()) {
                    // nobody waits and waiting, nobody in the ele, nobody
                    break;
                }
            } finally {
                queue.unlock();
            }
            //System.out.println("Direction : " + direction);
            selectMainRequest(high);
            boolean open = false;
            if (inside.containsKey(current)) {
                OutputThread.getInstance().println(
                        "OPEN-" + building + "-" + floor + "-" + id);
                open = true;
                sleep(200);
                dealWithInside(high);
            }
            // whether dor has been open
            if (outside.containsKey(current)) {
                open = dealWithOutside(open, high);
            }
            if (open) {
                sleep(200);
                if (outside.containsKey(current)) {
                    dealWithOutside(true, high);
                }
                OutputThread.getInstance().println(
                        "CLOSE-" + building + "-" + floor + "-" + id);
            }
            if (direction != 0) {
                boolean done = move(direction);
                if (done) {
                    OutputThread.getInstance().println(
                            "ARRIVE-" + building + "-" + floor + "-" + id);
                }
            }
        }
        //1-FROM-A-4-TO-C-3System.out.println(Thread.currentThread().getName() + " Over");
    }
    
    public void setFloor(int floor) {
        this.floor = floor;
    }
    
    public int getAvailable() {
        return available;
    }
    
    public int getSpeed() {
        return (int) (speed * 1000);
    }
    
    private boolean isCarry(MyRequest request) {
        return (request.getDestination() > current && direction == 1);
    }
}