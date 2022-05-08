public class VerticalElevator extends Elevator implements Runnable {
    public VerticalElevator(int id, Character building, int floor, int high) {
        super(id, building, floor, high);
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
            sleep(400);
            return true;
        } else if (direction == -1) {
            if (current == 1) {
                return false;
            }
            this.setCurrent(current - 1);
            this.setFloor(current - 1);
            sleep(400);
            return true;
        }
        return false;
    }
}
