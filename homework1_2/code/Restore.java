import java.util.HashMap;

public class Restore {
    private final String expr;
    private final HashMap<Character, FunctionFactor> funcs;
    
    public Restore(String expr, HashMap<Character, FunctionFactor> funcs) {
        this.expr = expr;
        this.funcs = funcs;
    }
    
    public int searchEnd(int start, StringBuilder exprNew) {
        int end = start;
        int cnt = 1;
        while (cnt != 0) {
            ++end;
            if (exprNew.charAt(end) == ')') {
                --cnt;
            } else if (exprNew.charAt(end) == '(') {
                ++cnt;
            }
        }
        return end;
    }
    
    public String show() {
        return restoreSum(restoreFunction());
    }
    
    //locate the self-defined function and expand it
    private String restoreFunction() {
        StringBuilder sb = new StringBuilder(this.expr);
        for (int i = 0; i < sb.length(); i++) {
            if (funcs.containsKey(sb.charAt(i))) {
                FunctionFactor func = funcs.get(sb.charAt(i));
                //split each parameter
                int end = searchEnd(i + 1, sb);
                int cnt = 0;
                String sub = sb.substring(i + 2, end);
                // System.out.println("sub is " + sub);
                for (String j : sub.split(",")) {
                    func.addParameters(func.getParameterOrder().get(cnt++), j);
                }
                int preLen = sb.length();
                String function = func.transform();
                // System.out.println(function);
                sb.replace(i + 2, end, function);
                sb.deleteCharAt(i);
                // System.out.println("the temp is " + sb);
                i = searchEnd(i, sb);
            }
        }
        return sb.toString();
    }
    
    private String restoreSum(String expr) {
        StringBuilder sb = new StringBuilder(expr);
        int pos = 0;
        while (sb.indexOf("sum", pos) != -1) {
            pos = sb.indexOf("sum", pos);
            int end = searchEnd(pos + 4, sb);
            String sub = sb.substring(pos + 4, end);
            String[] elems = sub.split(",");
            SumFactor sumFactor = new SumFactor(elems[1], elems[2], elems[3]);
            sb.replace(pos + 4, end, sumFactor.transform());
            sb.delete(pos, pos + 3);
            // System.out.println("the temp is " + sub);
            pos = searchEnd(pos, sb);
        }
        return sb.toString();
    }
    
}
