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
    
    public void printSword() {
        System.out.printf("The sword's id is %d, name is %s, sharpness is %s.%n"
                , this.getId(), this.getName().toString(), Double.toString(sharpness));
    }
}
