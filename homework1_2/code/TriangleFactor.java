import java.math.BigInteger;
import java.util.TreeMap;

public class TriangleFactor extends Factor {
    private final TreeMap<BigInteger, BigInteger> features;
    private int type; // 0 for sin, 1 for sin
    
    public TriangleFactor(BigInteger exp, TreeMap<BigInteger, BigInteger> features, String str) {
        super(exp);
        this.features = features;
        if (str.equals("sin")) {
            this.type = 0;
        } else {
            this.type = 1;
        }
    }
    
    public TreeMap<BigInteger, BigInteger> getFeatures() {
        return features;
    }
    
    @Override
    public boolean equals(Factor other) {
        if (other instanceof TriangleFactor) {
            TriangleFactor tmp = (TriangleFactor) other;
            return super.equals(tmp)
                    && (type == tmp.getType()) && features.equals(tmp.getFeatures());
        }
        return false;
    }
    
    public void reverse() {
        if (type == 0) {
            type = 1;
        } else {
            type = 0;
        }
    }
    
    public boolean weakEqual(TriangleFactor other) {
        return (type == (other.getType())) && features.equals(other.getFeatures());
    }
    
    public BigInteger contentExp() {
        return features.firstKey();
    }
    
    public boolean checkSquare() {
        return this.getExp().compareTo(BigInteger.valueOf(2)) == 0;
    }
    
    public boolean typeDifference(TriangleFactor other) {
        return (type != (other.getType())) && features.equals(other.getFeatures());
    }
    
    public int getType() {
        return type;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (type == 0) {
            sb.append("sin(");
        } else {
            sb.append("cos(");
        }
        for (BigInteger exp : features.keySet()) {
            if (exp.compareTo(BigInteger.ZERO) != 0) {
                sb.append("x");
                if (exp.compareTo(BigInteger.ONE) > 0) {
                    sb.append("**");
                    sb.append(exp);
                }
            } else {
                sb.append(features.get(exp));
            }
        }
        sb.append(")");
        if (this.getExp().compareTo(BigInteger.ONE) > 0) {
            sb.append("**");
            sb.append(this.getExp());
        }
        return sb.toString();
    }
}
