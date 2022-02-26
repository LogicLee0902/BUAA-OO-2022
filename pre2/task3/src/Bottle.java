public class Bottle extends Equipment {
    
    private double capacity;
    private boolean filled;
    
    public Bottle(int id, String name, long price, double capacity) {
        this.setId(id);
        this.setPrice(price);
        this.capacity = capacity;
        this.setName(name);
        this.filled = true;
    }
    
    public double getCapacity() {
        return capacity;
    }
    
    public boolean isFilled() {
        return filled;
    }
    
    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }
   
    public void printBottle() {
        System.out.printf(
                "The bottle's id is %d, name is %s, capacity is %s, filled is %s.%n"
                , this.getId(), this.getName().toString(), Double.toString(capacity),
                Boolean.toString(filled));
    }
}
