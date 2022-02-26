import java.math.BigInteger;

public class EpicSword extends Sword {
    
    private double evolveRatio;
    
    public EpicSword(int id, String name, BigInteger price, double sharpness, double evolveRatio) {
        super(id, name, price, sharpness);
        this.evolveRatio = evolveRatio;
    }
    
    @Override
    public String toString() {
        return String.format("The epicSword's id is %d, name is %s, sharpness is %s, " +
                        "evolveRatio is %s."
                , this.getId(), this.getName().toString(), Double.toString(this.getSharpness())
                , Double.toString(evolveRatio));
    }
    
    @Override
    public void use(Adventurer user) {
        super.use(user);
        this.setSharpness(this.getSharpness() * evolveRatio);
        System.out.println(this.getName() +
                "'s sharpness became " + this.getSharpness() +
                ".");
    }
}
