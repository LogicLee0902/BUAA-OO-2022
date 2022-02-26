public class RareSword extends Sword {
    
    private double extraBonus;
    
    public RareSword(int id, String name, long price, double sharpness, double extraBonus) {
        super(id, name, price, sharpness);
        this.extraBonus = extraBonus;
    }
    
    public void printRareSword() {
        System.out.printf("The rareSword's id is %d, name is %s, sharpness is %s" +
                        ", extraExpBonus is %s.%n"
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
