import java.math.BigInteger;

public class HealingPotion extends Bottle {
    
    private double efficiency;
    
    public HealingPotion(int id, String name, BigInteger price
            , double capacity, double efficiency) {
        super(id, name, price, capacity);
        this.efficiency = efficiency;
    }
    
    @Override
    public String toString() {
        return String.format(
                "The healingPotion's id is %d, name is %s, capacity is %s, filled is %s, " +
                        "efficiency is %s."
                , this.getId(), this.getName().toString(), Double.toString(this.getCapacity())
                , Boolean.toString(this.isFilled()), Double.toString(efficiency));
    }
    
    @Override
    public void use(Adventurer user) throws Exception {
        super.use(user);
        user.setHealth(user.getHealth() + this.getCapacity() * efficiency);
        System.out.println(user.getName() +
                " recovered extra " + this.getCapacity() * efficiency +
                ".");
    }
    
}
