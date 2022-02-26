public class Sword extends Equipment {
    
    private double sharpness;
    
    public Sword(int id, String name, long price, double sharpness) {
        this.setId(id);
        this.setName(name);
        this.setPrice(price);
        this.sharpness = sharpness;
    }
    
    public double getSharpness() {
        return sharpness;
    }
    
    public void setSharpness(double sharpness) {
        this.sharpness = sharpness;
    }
    
    public void printSword() {
        System.out.printf("The sword's id is %d, name is %s, sharpness is %s.%n"
                , this.getId(), this.getName().toString(), Double.toString(sharpness));
    }
    
    @Override
    public void use(Adventurer user) {
        user.setHealth(user.getHealth() - 10.0);
        user.setExp(user.getExp() + 10.0);
        user.setMoney(user.getMoney() + this.getSharpness());
        System.out.println(user.getName() +
                " used " + this.getName() +
                " and earned " + this.getSharpness() +
                ".");
    }
}
