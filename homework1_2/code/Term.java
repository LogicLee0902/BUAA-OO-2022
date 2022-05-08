import java.math.BigInteger;
import java.util.ArrayList;
import java.util.TreeMap;

public class Term {
    private BigInteger coefficient;
    private ArrayList<TriangleFactor> tris = new ArrayList<>();
    private VarFactor var;
    
    public Term(BigInteger exp, BigInteger coefficient) {
        this.var = new VarFactor(exp);
        TreeMap<BigInteger, BigInteger> init = new TreeMap<>();
        init.put(BigInteger.ZERO, BigInteger.ZERO);
        this.tris.add(new TriangleFactor(BigInteger.ONE, init, "cos"));
        this.coefficient = coefficient;
    }
    
    // overloading for sin/cos
    public Term(TreeMap<BigInteger, BigInteger> init, String type) {
        this.var = new VarFactor(BigInteger.ZERO);
        this.tris.add(new TriangleFactor(BigInteger.ONE, init, type));
        this.coefficient = BigInteger.ONE;
    }
    
    public void reverse() {
        this.coefficient = this.coefficient.negate();
    }
    
    public VarFactor getVar() {
        return var;
    }
    
    public BigInteger getCoefficient() {
        return coefficient;
    }
    
    public int addTypes(Term other) {
        if (!var.equals(other.getVar())) {
            return 0;
        }
        if (other.getTris().size() != tris.size()) {
            return 0;
        }
        int type = 1;
        //all same
        for (int i = 0; i < tris.size(); ++i) {
            if (!tris.get(i).equals(other.getTris().get(i))) {
                type = 0;
                break;
            }
        }
        if (type == 1) {
            this.setCoefficient(this.getCoefficient().add(other.getCoefficient()));
            return 1;
        }
        //sin**2 cos**2
        int cnt = 0;
        for (int i = 0; i < tris.size(); ++i) {
            if (tris.get(i).checkSquare()) {
                if (other.getTris().get(i).checkSquare()) {
                    type = 1;
                    cnt = i;
                }
                break;
            } else if (!tris.get(i).equals(other.getTris().get(i))) {
                return 0;
            }
        }
        if (type == 1) {
            int total = 0;
            int idx1 = 0;
            int idx2 = 0;
            for (int i = cnt; i < tris.size(); ++i) {
                boolean flag = false;
                for (int j = cnt; j < tris.size(); ++j) {
                    if (tris.get(i).equals(other.getTris().get(j))) {
                        flag = true;
                        break;
                    } else if (tris.get(i).typeDifference(other.getTris().get(j))) {
                        flag = true;
                        idx1 = i;
                        idx2 = j;
                        total++;
                        break;
                    }
                }
                if (!flag || total > 1) {
                    return 0;
                }
            }
            this.mergeTri(other, idx1, idx2);
        }
        return 0;
    }
    
    public void sort() {
        // first exp, then sin/cos, then contents
        tris.sort((x, y) -> {
            BigInteger expX = x.getExp().compareTo(BigInteger.valueOf(2)) == 0
                    ? BigInteger.ONE.negate() : x.getExp();
            BigInteger expY = y.getExp().compareTo(BigInteger.valueOf(2)) == 0
                    ? BigInteger.ONE.negate() : y.getExp();
            if (expX.compareTo(expY) > 0) {
                return -1;
            } else if (expX.compareTo(expY) < 0) {
                return 1;
            }
            if (x.getType() > y.getType()) {
                return 1;
            } else if (x.getType() < y.getType()) {
                return -1;
            }
            return x.contentExp().compareTo(y.contentExp());
        });
    }
    
    public ArrayList<TriangleFactor> getTris() {
        return tris;
    }
    
    public void setCoefficient(BigInteger coefficient) {
        this.coefficient = coefficient;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (coefficient.abs().compareTo(BigInteger.ONE) > 0) {
            sb.append(coefficient);
        } else if (coefficient.negate().compareTo(BigInteger.ONE) == 0) {
            sb.append("-");
        }
        for (TriangleFactor i : tris) {
            if (sb.length() > 0 && !sb.toString().equals("-")) {
                sb.append("*");
            }
            sb.append(i);
        }
        if (!var.toString().equals("1")) {
            if (sb.length() > 0 && !sb.toString().equals("-")) {
                sb.append("*");
            }
            sb.append(var);
        } else if (sb.length() == 0 || sb.toString().equals("-")) {
            sb.append(var);
        }
        return sb.toString();
    }
    
    public void simplify() {
        TreeMap<BigInteger, BigInteger> zero = new TreeMap<>();
        zero.put(BigInteger.ZERO, BigInteger.ZERO);
        TriangleFactor base = new TriangleFactor(BigInteger.ONE, zero, "cos");
        tris.removeIf(i -> i.weakEqual(base));
    }
    
    public void mergeTri(Term other, int idx1, int idx2) {
        if (other.getCoefficient().compareTo(coefficient) >= 0) {
            this.getTris().remove(idx1);
            other.setCoefficient(other.getCoefficient().subtract(coefficient));
        } else {
            tris.remove(idx1);
            BigInteger coeff = other.getCoefficient();
            other.setCoefficient(coefficient.subtract(coeff));
            other.getTris().get(idx2).reverse();
            coefficient = coeff;
        }
        
    }
}

