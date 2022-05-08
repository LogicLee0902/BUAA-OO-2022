import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SumFactor implements Substitution {
    private BigInteger low;
    private BigInteger high;
    private StringBuilder expr = new StringBuilder();
    private static final Pattern PATTERN = Pattern.compile("i");
    private final String formula;
    
    public SumFactor(String low, String high, String formula) {
        this.low = new BigInteger(low);
        this.high = new BigInteger(high);
        this.formula = formula;
    }
    
    public String sub(String factor) {
        return "(".concat(factor).concat(")");
    }
    
    private String substitution(BigInteger index) {
        Matcher matcher = PATTERN.matcher(formula);
        StringBuilder s = new StringBuilder(formula);
        while (matcher.find()) {
            int start = matcher.start();
            if (start > 0 && s.charAt(start - 1) == 's') {
                continue;
            }
            String replacement = "(".concat(index.toString()).concat(")");
            s.replace(start, start + 1, replacement);
            matcher = PATTERN.matcher(s);
        }
        return s.toString();
    }
    
    public String transform() {
        if (low.compareTo(high) > 0) {
            return "0";
        }
        expr.append(sub(substitution(low)));
        for (BigInteger i = low.add(BigInteger.ONE);
             i.compareTo(high) <= 0; i = i.add(BigInteger.ONE)) {
            expr.append("+");
            expr.append(sub(substitution(i)));
        }
        return expr.toString();
    }
    
}
