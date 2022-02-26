public class Bottle {
    private String name;
    private int id;
    private long price;
    private double capacity;
    private boolean filled;
    
    public Bottle(int id, String name,long price, double capacity) {
        this.id = id;
        this.price = price;
        this.capacity = capacity;
        this.name = name;
        this.filled = true;
    }
    
    public double getCapacity() {
        return capacity;
    }
    
    public boolean isFilled() {
        return filled;
    }
    
    public void setFilled(boolean filled) {
        this.filled = filled;
    }
    
    public long getPrice() {
        return price;
    }
    
    public void setPrice(long price) {
        this.price = price;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void returnInformain() {
        System.out.printf(
                "The bottle's id is %d, name is %s, capacity is %s, filled is %s.%n"
                , id, name.toString(), Double.toString(capacity), Boolean.toString(filled));
    }
}
