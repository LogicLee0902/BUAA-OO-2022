public class EpicSword extends Sword {
    
    private double evolveRatio;
    
    public EpicSword(int id, String name, long price, double sharpness, double evolveRatio) {
        super(id, name, price, sharpness);
        this.evolveRatio = evolveRatio;
    }
    
    public void printEpicSword() {
        System.out.printf("The epicSword's id is %d, name is %s, sharpness is %s, " +
                        "evolveRatio is %s.%n"
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
