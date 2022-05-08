import com.oocourse.elevator3.Request;

public class VerticalElevator extends Elevator implements Runnable {
    public VerticalElevator(int id, Character building, int floor, int high
            , int available, double speed, int capacity, LockArrayList<Request> requests) {
        super(id, building, floor, high, available, speed, capacity, requests);
    }
    
    @Override
    public void changeDirection(int target) {
        this.setDirection(Integer.compare(target, this.getCurrent()));
    }
    
    @Override
    public boolean move(int direction) {
        int current = this.getCurrent();
        if (direction == 1) {
            if (current == 10) {
                return false;
            }
            this.setCurrent(current + 1);
            this.setFloor(current + 1);
            sleep(this.getSpeed());
            return true;
        } else if (direction == -1) {
            if (current == 1) {
                return false;
            }
            this.setCurrent(current - 1);
            this.setFloor(current - 1);
            sleep(this.getSpeed());
            return true;
        }
        return false;
    }
}
