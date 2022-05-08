public class HorizontalElevator extends Elevator implements Runnable {
    
    public HorizontalElevator(int id, Character building, int floor, int high) {
        super(id, building, floor, high);
    }
    
    @Override
    public void changeDirection(int target) {
        int current = this.getCurrent();
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
            this.setDirection(0);
        } else if (Integer.compare(macro - current, current - micro) == 1) {
            this.setDirection(-1);
        } else {
            this.setDirection(1);
        }
    }
    
    @Override
    public boolean move(int direction) {
        int current = this.getCurrent();
        //System.out.println("direction : " + direction + " current " + current);
        if (direction == 1) {
            current = (current) % 5 + 1;
            this.setCurrent(current);
            this.setBuilding((char) ('A' + current - 1));
            sleep(200);
            return true;
        } else if (direction == -1) {
            current = (current + 3) % 5 + 1;
            this.setCurrent(current);
            this.setBuilding((char) ('A' + current - 1));
            sleep(200);
            return true;
        }
        return false;
    }
}
