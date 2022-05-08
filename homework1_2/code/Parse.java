import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parse {
    private final List<String> exprs = new ArrayList<>();
    private final ArrayList<String> suffix;
    private final Stack<String> stack = new Stack<>();
    private final int count;
    
    public Parse(SufExpr sufExpr) {
        this.suffix = sufExpr.getSufExpr();
        this.count = setExprs();
        
    }
    
    public int setExprs() {
        String id;
        int cnt = 0;
        for (String i : suffix) {
            switch (i) {
                case "*":
                case "**":
                    id = "f" + (++cnt);
                    addMulOrPow(i, id);
                    break;
                case "+":
                case "-":
                    if (!stack.empty()) {
                        id = "f" + (++cnt);
                        addAddOrSub(i, id);
                    } else {
                        stack.push(i);
                    }
                    break;
                case "++":
                case "--":
                case "+++":
                case "---":
                    if (!stack.empty()) {
                        id = "f" + (++cnt);
                        addNegOrPos(i, id);
                    } else {
                        stack.push(i);
                    }
                    break;
                case "sin":
                case "cos":
                    id = "f" + (++cnt);
                    addTriangle(i, id);
                    break;
                default:
                    stack.push(i);
            }
        }
        cnt = dealReminder(cnt);
        return cnt;
    }
    
    private void addAddOrSub(String i, String id) {
        StringBuilder line = new StringBuilder();
        line.append(id).append(" ");
        if (i.equals("+")) {
            line.append("add ");
        } else {
            line.append("sub ");
        }
        getOperator(id, line);
    }
    
    private void addMulOrPow(String i, String id) {
        StringBuilder line = new StringBuilder();
        line.append(id).append(" ");
        if (i.equals("*")) {
            line.append("mul ");
        } else {
            line.append("pow ");
        }
        getOperator(id, line);
    }
    
    private void addTriangle(String i, String id) {
        StringBuilder line = new StringBuilder();
        line.append(id).append(" ");
        if (i.equals("sin")) {
            line.append("sin ");
        } else {
            line.append("cos ");
        }
        line.append(stack.pop());
        stack.push(id);
        exprs.add(line.toString());
    }
    
    private void addNegOrPos(String i, String id) {
        StringBuilder line = new StringBuilder();
        line.append(id).append(" ");
        if (i.equals("++") || i.equals("+++")) {
            line.append("pos ");
        } else {
            line.append("neg ");
        }
        line.append(stack.pop());
        stack.push(id);
        exprs.add(line.toString());
    }
    
    //deal with two operands
    private void getOperator(String id, StringBuilder sb) {
        String pop;
        pop = stack.pop();
        sb.append(stack.pop());
        sb.append(" ");
        sb.append(pop);
        exprs.add(sb.toString());
        stack.push(id);
    }
    
    private int dealReminder(int total) {
        int cnt = total;
        String id;
        StringBuilder line = new StringBuilder();
        while (!stack.empty()) {
            if (suffix.size() == 1) {
                // means only hava one elem
                id = "f" + (++cnt);
                line.append(id).append(" ");
                line.append(stack.pop());
            } else if (stack.size() == 1) {
                break;
            } else {
                //means there is something about the head first
                id = "f" + (++cnt);
                line.append(id).append(" ");
                String num = stack.pop();
                if (stack.peek().equals("++")) {
                    line.append("pos ");
                } else {
                    line.append("neg ");
                }
                line.append(num);
                stack.pop();
            }
            exprs.add(line.toString());
            
        }
        return cnt;
    }
    
    public List<String> getExprs() {
        return exprs;
    }
    
    public int getN() {
        return count;
    }
}
