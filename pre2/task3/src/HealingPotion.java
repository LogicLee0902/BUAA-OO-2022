public class HealingPotion extends Bottle {
    
    private double efficiency;
    
    public HealingPotion(int id, String name, long price, double capacity, double efficiency) {
        super(id, name, price, capacity);
        this.efficiency = efficiency;
    }
    
    public void printHealingPotion() {
        System.out.printf(
                "The healingPotion's id is %d, name is %s, capacity is %s, filled is %s, " +
                        "efficiency is %s.%n"
                , this.getId(), this.getName().toString(), Double.toString(this.getCapacity())
                , Boolean.toString(this.isFilled()), Double.toString(efficiency));
    }
    
}
