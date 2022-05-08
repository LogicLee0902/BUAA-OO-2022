import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class FunctionFactor implements Substitution {
    
    private final HashMap<String, String> parameters = new HashMap<>();
    private String expr;
    private final ArrayList<String> parameterOrder = new ArrayList<>();
    
    public FunctionFactor(String expr) {
        this.expr = expr.substring(expr.indexOf('=') + 1);
        parameterOrder.addAll(Arrays.asList(expr.substring(expr.indexOf('(') + 1
                , expr.indexOf(')')).split(",")));
    }
    
    public String getExpr() {
        return expr;
    }
    
    public ArrayList<String> getParameterOrder() {
        return parameterOrder;
    }
    
    public String sub(String factor) {
        return "(".concat(factor).concat(")");
    }
    
    public String transform() {
        StringBuilder realExpr = new StringBuilder(this.expr);
        int pos = 0;
        while (pos < realExpr.length()) {
            String str = Character.toString(realExpr.charAt(pos));
            if (parameters.containsKey(str)) {
                realExpr.replace(pos, pos + 1, sub(parameters.get(str)));
                pos = pos + parameters.get(str).length() + 1;
            }
            pos++;
        }
        return realExpr.toString();
    }
    
    public void addParameters(String formal, String actual) {
        // System.out.println("formal: " + formal + " actual: " + actual);
        this.parameters.put(formal, actual);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FunctionFactor that = (FunctionFactor) o;
        return Objects.equals(parameters, that.parameters)
                && Objects.equals(expr, that.expr)
                && Objects.equals(parameterOrder, that.parameterOrder);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(parameters, expr, parameterOrder);
    }
}