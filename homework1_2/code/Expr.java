import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.StringJoiner;

public class Expr {
    private ArrayList<Term> terms;
    
    public Expr() {
        this.terms = new ArrayList<>();
    }
    
    public void add(Term term) {
        boolean flag = true;
        term.sort();
        for (Term i : terms) {
            // System.out.println("Compare " + i + " between " + term);
            if (i.addTypes(term) == 1) {
                flag = false;
                //i.setCoefficient(i.getCoefficient().add(term.getCoefficient()));
                break;
            }
        }
        if (flag) {
            this.terms.add(term);
        }
    }
    
    public ArrayList<Term> getTerms() {
        return terms;
    }
    
    public void setTerms(ArrayList<Term> terms) {
        this.terms = terms;
    }
    
    public void neg() {
        for (Term term : terms) {
            term.reverse();
        }
    }
    
    //sort for easy search()
    public void sort() {
        terms.sort((x, y) -> {
            if (x.getVar().getExp().compareTo(y.getVar().getExp()) > 0) {
                return 1;
            } else if (x.getVar().getExp().compareTo(y.getVar().getExp()) < 0) {
                return -1;
            }
            return 0;
        });
    }
    
    // get the sin or cos of the expr
    public void triangulation(String type) {
        TreeMap<BigInteger, BigInteger> features = new TreeMap<>();
        for (Term term : terms) {
            features.put(term.getVar().getExp(), term.getCoefficient());
        }
        this.terms.clear();
        this.terms.add(new Term(features, type));
    }
    
    public Term merge(Term term1, Term term2) {
        Term termNew = new Term(BigInteger.ZERO, BigInteger.ONE);
        // System.out.println("In: " + term1 + " " + term2);
        termNew.setCoefficient(term1.getCoefficient().multiply(term2.getCoefficient()));
        addTri(term1, termNew);
        addTri(term2, termNew);
        //System.out.println("Out：" + term1 + " " + term2);
        termNew.getVar().setExp(term2.getVar().getExp().add(term1.getVar().getExp()));
        //System.out.println("Result is " + termNew);
    
        TreeMap<BigInteger, BigInteger> zero = new TreeMap<>();
        zero.put(BigInteger.ZERO, BigInteger.ZERO);
        TriangleFactor base = new TriangleFactor(BigInteger.ONE, zero, "cos");
        for (TriangleFactor i : termNew.getTris()) {
            if (i.weakEqual(base)) {
                i.setExp(BigInteger.ONE);
                break;
            }
        }
        return termNew;
    }
    
    private void addTri(Term term, Term termNew) {
        for (TriangleFactor i : term.getTris()) {
            boolean flag = false;
            for (TriangleFactor j : termNew.getTris()) {
                if (i.weakEqual(j)) {
                    j.setExp(j.getExp().add(i.getExp()));
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                String type = (i.getType() == 0) ? "sin" : "cos";
                TriangleFactor tri = new TriangleFactor(i.getExp(), i.getFeatures(), type);
                termNew.getTris().add(tri);
            }
        }
        // System.out.println("term After: " + term);
    }
    
    
    //toString
    @Override
    public String toString() {
        if (terms.size() == 0) {
            return "0";
        }
        StringJoiner pos = new StringJoiner("+");
        for (Term term : terms) {
            if (term.getCoefficient().compareTo(BigInteger.ZERO) > 0) {
                pos.add(term.toString());
            }
        }
        StringJoiner neg = new StringJoiner("");
        for (Term term : terms) {
            if (term.getCoefficient().compareTo(BigInteger.ZERO) < 0) {
                neg.add(term.toString());
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(pos);
        sb.append(neg);
        return sb.toString();
    }
    
    public void simplify() {
        TreeMap<BigInteger, BigInteger> base = new TreeMap<>();
        base.put(BigInteger.ZERO, BigInteger.ZERO);
        TriangleFactor tri = new TriangleFactor(BigInteger.ONE, base, "sin");
        terms.removeIf(i -> i.getCoefficient().compareTo(BigInteger.ZERO) == 0);
        Iterator<Term> iter = terms.iterator();
        while (iter.hasNext()) {
            Term term = iter.next();
            for (TriangleFactor i : term.getTris()) {
                if (i.weakEqual(tri)) {
                    iter.remove();
                    break;
                }
            }
        }
    }
}
