import java.math.BigInteger;

public class Bottle extends Equipment {
    
    private double capacity;
    private boolean filled;
    
    public Bottle(int id, String name, BigInteger price, double capacity) {
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
    
    public void setFilled(boolean filled) {
        this.filled = filled;
    }
    
    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }
   
    @Override
    public String toString() {
        return String.format(
                "The bottle's id is %d, name is %s, capacity is %s, filled is %s."
                , this.getId(), this.getName().toString(), Double.toString(capacity),
                Boolean.toString(filled));
    }
    
    @Override
    public void use(Adventurer user) throws Exception  {
        if (!this.filled) {
            throw new Exception("Failed to use " + getName() + " because it is empty.");
        }
        user.setHealth(user.getHealth() + capacity / 10);
        setFilled(false);
        this.setPrice(this.getPrice().divide(BigInteger.TEN));
        System.out.println(user.getName() +
                " drank " + getName() +
                " and recovered " + capacity / 10.0 +
                ".");
    }
    
}
