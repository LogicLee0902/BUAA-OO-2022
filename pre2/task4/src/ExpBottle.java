public class ExpBottle extends Bottle {
    private double expRatio;
    
    public ExpBottle(int id, String name, long price, double capacity, double expRatio) {
        super(id, name, price, capacity);
        this.expRatio = expRatio;
    }
    
    public void printExpBottle() {
        System.out.printf(
                "The expBottle's id is %d, name is %s, capacity is %s, filled is %s, " +
                        "expRatio is %s.%n"
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
