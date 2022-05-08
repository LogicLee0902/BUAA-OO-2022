import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.util.Comparator;
import java.util.Arrays;

public class Poly {
    private static String FINAL;
    private final HashMap<String, Poly> polys;
    private HashMap<Integer, BigInteger> features;
    private final int cnt;
    
    public Poly(int n) {
        this.features = new HashMap<>();
        this.polys = new HashMap<>();
        FINAL = "f" + n;
        cnt = n;
    }
    
    public void add(Abstract newOne) {
        String id = newOne.getId();
        String op = newOne.getOp();
        String factor = newOne.getFactor();
        String aim = newOne.getAim();
        Poly polyNew = new Poly(cnt);
        switch (op) {
            case "null":
                if (!factor.equals("x")) {
                    BigInteger coeff = new BigInteger(factor);
                    polyNew.setFeatures(0, coeff);
                } else {
                    polyNew.setFeatures(1, BigInteger.ONE);
                }
                break;
            case "pos":
                HashMap<Integer, BigInteger> featureFac = build(factor);
                polyNew.setFeatures(featureFac);
                break;
            case "neg":
                featureFac = minus(build(factor));
                polyNew.setFeatures(featureFac);
                break;
            case "add":
                featureFac = build(factor);
                HashMap<Integer, BigInteger> featureAim = build(aim);
                polyNew = addMerge(featureFac, featureAim);
                break;
            case "sub":
                featureFac = build(factor);
                featureAim = minus(build(aim));
                polyNew = addMerge(featureFac, featureAim);
                break;
            case "mul":
                featureFac = build(factor);
                featureAim = build(aim);
                polyNew = mulMerge(featureFac, featureAim);
                break;
            case "pow":
                featureFac = build(factor);
                featureAim = new HashMap<>();
                featureAim.put(0, BigInteger.ONE);
                int pow = Integer.parseInt(aim);
                while (pow > 0) {
                    Poly polyTmp = mulMerge(featureFac, featureAim);
                    featureAim.clear();
                    HashMap<Integer, BigInteger> mapTemp = polyTmp.getFeatures();
                    for (Integer k : mapTemp.keySet()) {
                        featureAim.put(k, mapTemp.get(k));
                    }
                    pow--;
                }
                polyNew.setFeatures(featureAim);
                break;
            default:
        }
        polys.put(id, polyNew);
    }
    
    private Poly mulMerge(HashMap<Integer, BigInteger> featureFac,
                          HashMap<Integer, BigInteger> featureAim) {
        Poly polyNew = new Poly(cnt);
        for (Map.Entry<Integer, BigInteger> i : featureFac.entrySet()) {
            for (Map.Entry<Integer, BigInteger> j : featureAim.entrySet()) {
                polyNew.setFeatures(i.getKey() + j.getKey(), i.getValue().multiply(j.getValue()));
            }
        }
        return polyNew;
    }
    
    private Poly addMerge(HashMap<Integer, BigInteger> featureFac,
                          HashMap<Integer, BigInteger> featureAim) {
        Poly polyNew = new Poly(cnt);
        for (Map.Entry<Integer, BigInteger> i : featureFac.entrySet()) {
            polyNew.setFeatures(i.getKey(), i.getValue());
        }
        for (Map.Entry<Integer, BigInteger> i : featureAim.entrySet()) {
            polyNew.setFeatures(i.getKey(), i.getValue());
        }
        return polyNew;
    }
    
    private HashMap<Integer, BigInteger> build(String operator) {
        HashMap<Integer, BigInteger> newFeature = new HashMap<>();
        if (operator.charAt(0) == 'f') {
            newFeature = polys.get(operator).getFeatures();
        } else if (operator.equals("x")) {
            newFeature.put(1, BigInteger.ONE);
        } else {
            BigInteger coeff = new BigInteger(operator);
            newFeature.put(0, coeff);
        }
        return newFeature;
    }
    
    private HashMap<Integer, BigInteger> minus(HashMap<Integer, BigInteger> features) {
        features.replaceAll((i, v) -> v.negate());
        return features;
    }
    
    public HashMap<Integer, BigInteger> getFeatures() {
        return features;
    }
    
    public void setFeatures(Integer exp, BigInteger coefficient) {
        if (features.get(exp) == null) {
            features.put(exp, coefficient);
            // if the exp is new, then add directly
        } else {
            BigInteger value = features.get(exp);
            value = value.add(coefficient);
            features.put(exp, value);
            // else merge the term with the same idx
        }
    }
    
    // Overloading
    public void setFeatures(HashMap<Integer, BigInteger> features) {
        this.features = features;
    }
    
    // remove the zero
    private HashMap<Integer, BigInteger> clearZero(HashMap<Integer, BigInteger> map) {
        map.entrySet().removeIf(item -> item.getValue().equals(BigInteger.ZERO));
        return map;
    }
    
    private String addTerm(int exp, BigInteger num) {
        StringBuilder sb = new StringBuilder();
        if (num.abs().equals(BigInteger.ONE)) {
            if (num.equals(BigInteger.ONE)) {
                sb.append("x");
            }
            else {
                sb.append("-x");
            }
        } else {
            sb.append(num);
            sb.append("*x");
        }
        if (exp > 1) {
            sb.append("**");
            sb.append(exp);
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        //get the final one
        Poly poly = polys.get(FINAL);
        //remove the terms which has the 0 coefficient
        HashMap<Integer, BigInteger> info = clearZero(poly.getFeatures());
        if (info.isEmpty()) {
            return "0";
        }
        // sort by key
        Set<Integer> exps = poly.getFeatures().keySet();
        Set<Integer> sort = new TreeSet<>(Comparator.reverseOrder());
        sort.addAll(exps);
        List<Integer> sorted = Arrays.asList(sort.toArray(new Integer[0]));
        // build the String to print
        int first = sorted.get(0);
        if (sorted.size() == 1) {
            int exp = sorted.get(0);
            if (exp >= 1) {
                BigInteger num = info.get(first);
                out.append(addTerm(first, num));
            } else {
                out.append(info.get(0));
            }
            return out.toString();
        }
        out.append(addTerm(first, info.get(first)));
        for (int i = 1; i < sorted.size(); ++i) {
            if (info.get(sorted.get(i)).compareTo(BigInteger.ZERO) > 0) {
                out.append("+");
            }
            if (sorted.get(i) > 0) {
                out.append(addTerm(sorted.get(i), info.get(sorted.get(i))));
            }
            else {
                out.append(info.get(sorted.get(i)));
            }
        }
        return out.toString();
    }
}
