import java.math.BigInteger;

public abstract class Equipment implements ValuableObject {
    private int id;
    private String name;
    private BigInteger price;
    
    public Equipment() {
    }
    
    public int getId() {
        return id;
    }
    
    public BigInteger getPrice() {
        return price;
    }
    
    public String getName() {
        return name;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPrice(BigInteger price) {
        this.price = price;
    }
    
    public abstract String toString();
    
    public void use(Adventurer user) throws Exception {
    }
    
}
    

