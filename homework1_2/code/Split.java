import java.util.ArrayList;

public class Split {
    private final ArrayList<String> operators;
    private final String expr;
    
    public Split(String expr) {
        this.expr = expr;
        this.operators = new ArrayList<>();
        int pos = dealWithHead(0);
        while (pos < expr.length()) {
            int start;
            if (Character.isDigit(expr.charAt(pos))) {
                start = pos;
                pos = splitNum(pos);
                this.operators.add(expr.substring(start, pos));
                pos--;
            } else if ((expr.charAt(pos) == '+') || (expr.charAt(pos) == '-')) {
                if (!preDigit(pos - 1)) {
                    if (sufDigit(pos + 1) &&
                            isTerm(pos)) {
                        start = pos;
                        pos = splitNum(pos);
                        String part = expr.substring(start, pos);
                        this.operators.add(part);
                        pos--;
                    } else {
                        this.operators.add(expr.charAt(pos) + Character.toString(expr.charAt(pos)));
                    }
                } else {
                    this.operators.add(Character.toString(expr.charAt(pos)));
                }
            } else if (expr.charAt(pos) == 'x') {
                this.operators.add("x");
            } else if (expr.charAt(pos) == '*') {
                if (expr.charAt(pos + 1) == '*') {
                    this.operators.add("**");
                    pos++;
                } else {
                    this.operators.add("*");
                }
            } else if (expr.charAt(pos) == 's' || expr.charAt(pos) == 'c') {
                pos = dealWithTriangle(pos);
            } else if (expr.charAt(pos) == '(') {
                this.operators.add(Character.toString(expr.charAt(pos)));
                pos = dealWithHead(pos + 1);
                --pos;
            } else if (expr.charAt(pos) == ')') {
                this.operators.add(Character.toString(expr.charAt(pos)));
            }
            pos++;
        }
    }
    
    private boolean isTerm(int pos) {
        return expr.charAt(pos - 2) == '+'
                || expr.charAt(pos - 2) == '-' || expr.charAt(pos - 1) == '*';
    }
    
    public int dealWithHead(int start) {
        int pos = start;
        int cnt = 3;
        StringBuilder sb = new StringBuilder();
        while ((expr.charAt(pos) == '+' || expr.charAt(pos) == '-') && (pos - start) < 2) {
            sb.delete(0, sb.length());
            for (int i = 0; i < cnt; ++i) {
                sb.append(Character.toString(expr.charAt(pos)));
            }
            ++pos;
            --cnt;
            this.operators.add(sb.toString());
        }
        return pos;
    }
    
    public int dealWithTriangle(int start) {
        if (expr.charAt(start) == 's' && expr.charAt(start + 1) != '(') {
            this.operators.add("sin");
        } else {
            this.operators.add("cos");
        }
        return start + 2;
    }
    
    private boolean preDigit(int pos) {
        int start = pos;
        if (start < 0) {
            return false;
        }
        while (((expr.charAt(start) == ' ') ||
                (expr.charAt(start) == '\t') || (expr.charAt(start) == ')'))
                && start > 0) {
            --start;
        }
        return Character.isDigit(expr.charAt(start)) || expr.charAt(start) == 'x';
    }
    
    private boolean sufDigit(int pos) {
        int start = pos;
        if (start >= expr.length()) {
            return false;
        }
        while (((expr.charAt(start) == ' ') || (expr.charAt(start) == '\t')) && start > 0) {
            ++start;
        }
        return Character.isDigit(expr.charAt(start)) || expr.charAt(start) == 'x';
    }
    
    private int splitNum(int pos) {
        int start = pos;
        if (expr.charAt(start) == '+' || expr.charAt(start) == '-') {
            ++start;
        }
        
        while ((Character.isDigit(expr.charAt(start)) || expr.charAt(start) == 'x')
                && start < expr.length() - 1) {
            start++;
        }
        if (start == expr.length() - 1
                && (Character.isDigit(expr.charAt(start)) || expr.charAt(start) == 'x')) {
            return start + 1;
        }
        return start;
    }
    
    public ArrayList<String> getOperators() {
        return operators;
    }
}
