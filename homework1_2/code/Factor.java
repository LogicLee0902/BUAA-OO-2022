import java.math.BigInteger;

public class Factor {
    private BigInteger exp;
    
    public Factor(BigInteger exp) {
        this.exp = exp;
    }
    
    public BigInteger getExp() {
        return exp;
    }
    
    public void setExp(BigInteger exp) {
        this.exp = exp;
    }
    
    public boolean equals(Factor other) {
        return this.exp.equals(other.getExp());
    }
}
