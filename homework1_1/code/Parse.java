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
        StringBuilder line = new StringBuilder();
        String id;
        int cnt = 0;
        for (String i : suffix) {
            switch (i) {
                case "*":
                case "**":
                    line.delete(0, line.length());
                    id = "f" + (++cnt);
                    line.append(id).append(" ");
                    if (i.equals("*")) {
                        line.append("mul ");
                    } else {
                        line.append("pow ");
                    } getOperator(id, line);
                    break;
                case "+":
                case "-":
                    line.delete(0, line.length());
                    if (!stack.empty()) {
                        id = "f" + (++cnt);
                        line.append(id).append(" ");
                        if (i.equals("+")) {
                            line.append("add ");
                        } else {
                            line.append("sub ");
                        }
                        getOperator(id, line);
                    } else {
                        stack.push(i);
                    }
                    break;
                case "++":
                case "--":
                case "+++":
                case "---":
                    if (!stack.empty()) {
                        line.delete(0, line.length());
                        id = "f" + (++cnt);
                        line.append(id).append(" ");
                        if (i.equals("++") || i.equals("+++")) {
                            line.append("pos ");
                        } else {
                            line.append("neg ");
                        }
                        line.append(stack.pop());
                        stack.push(id);
                        exprs.add(line.toString());
                    } else {
                        stack.push(i);
                    }
                    break;
                default:
                    stack.push(i);
            }
        }
        cnt = dealReminder(cnt);
        return cnt;
    }
    
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
