import java.math.BigInteger;

interface ValuableObject {
    public void use(Adventurer user) throws Exception;
    
    public int getId();
    
    public BigInteger getPrice();
    
    public String getName();
    
    @Override
    public String toString();
}
