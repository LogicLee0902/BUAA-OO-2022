import com.oocourse.elevator3.Request;

public class HorizontalElevator extends Elevator implements Runnable {
    
    public HorizontalElevator(int id, Character building, int floor, int high
            , int available, double speed, int capacity, LockArrayList<Request> requests) {
        super(id, building, floor, high, available, speed, capacity, requests);
    }
    
    @Override
    public void changeDirection(int target) {
        int current = this.getCurrent();
        this.setDirection(calculateDistance(target, current));
    }
    
    private int calculateDistance(int target, int current) {
        int macro;
        int micro;
        if (target < current) {
            macro = target + 5;
            micro = target;
        } else if (target > current) {
            macro = target;
            micro = target - 5;
        } else {
            macro = micro = target;
        }
        if (target == current) {
            return 0;
        } else if (Integer.compare(macro - current, current - micro) == 1) {
            return -1;
        } else {
            return 1;
        }
    }
    
    @Override
    public boolean move(int direction) {
        int current = this.getCurrent();
        if (direction == 1) {
            current = (current) % 5 + 1;
            this.setCurrent(current);
            this.setBuilding((char) ('A' + current - 1));
            sleep(this.getSpeed());
            return true;
        } else if (direction == -1) {
            current = (current + 3) % 5 + 1;
            this.setCurrent(current);
            this.setBuilding((char) ('A' + current - 1));
            sleep(this.getSpeed());
            return true;
        }
        return false;
    }
}
