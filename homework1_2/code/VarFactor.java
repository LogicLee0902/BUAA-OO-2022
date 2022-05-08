import java.math.BigInteger;

public class VarFactor extends Factor {
    public VarFactor(BigInteger exp) {
        super(exp);
    }
    
    @Override
    public String toString() {
        if (this.getExp().compareTo(BigInteger.ZERO) == 0) {
            return "1";
        } else if (this.getExp().compareTo(BigInteger.ONE) == 0) {
            return "x";
        }
        return "x" + "**" + this.getExp();
    }
}
