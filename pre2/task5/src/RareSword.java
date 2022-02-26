import java.math.BigInteger;

public class RareSword extends Sword {
    
    private double extraBonus;
    
    public RareSword(int id, String name, BigInteger price, double sharpness, double extraBonus) {
        super(id, name, price, sharpness);
        this.extraBonus = extraBonus;
    }
    
    @Override
    public String toString() {
        return String.format("The rareSword's id is %d, name is %s, sharpness is %s" +
                        ", extraExpBonus is %s."
                , this.getId(), this.getName().toString(), Double.toString(this.getSharpness())
                , Double.toString(extraBonus));
    }
    
    @Override
    public void use(Adventurer user) {
        super.use(user);
        user.setExp(user.getExp() + extraBonus);
        System.out.println(user.getName() +
                " got extra exp " + extraBonus +
                ".");
    }
}
