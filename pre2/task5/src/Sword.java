import java.math.BigInteger;

public class Sword extends Equipment {
    
    private double sharpness;
    
    public Sword(int id, String name, BigInteger price, double sharpness) {
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
    
    @Override
    public String toString() {
        return String.format("The sword's id is %d, name is %s, sharpness is %s."
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
