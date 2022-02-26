import java.math.BigInteger;

public class ExpBottle extends Bottle {
    private double expRatio;
    
    public ExpBottle(int id, String name, BigInteger price, double capacity, double expRatio) {
        super(id, name, price, capacity);
        this.expRatio = expRatio;
    }
    
    @Override
    public String toString() {
        return String.format(
                "The expBottle's id is %d, name is %s, capacity is %s, filled is %s, " +
                        "expRatio is %s."
                , this.getId(), this.getName().toString(), Double.toString(this.getCapacity())
                , Boolean.toString(this.isFilled()), Double.toString(expRatio));
    }
    
    @Override
    public void use(Adventurer user) throws Exception {
        super.use(user);
        user.setExp(user.getExp() * expRatio);
        System.out.println(user.getName() +
                "'s exp became " + user.getExp() +
                ".");
    }
    
}
