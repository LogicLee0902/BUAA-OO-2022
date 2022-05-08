import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class SufExpr {
    private final ArrayList<String> operators;
    private final ArrayList<String> sufExpr = new ArrayList<>();
    private final Stack<String> stackSuf = new Stack<>();
    private static final HashMap<String, Integer> PRIORITY = new HashMap<>();
    
    public SufExpr(Split split) {
        this.operators = split.getOperators();
        PRIORITY.put("sin", 6);
        PRIORITY.put("cos", 6);
        PRIORITY.put("**", 5);
        PRIORITY.put("*", 4);
        PRIORITY.put("++", 3);
        PRIORITY.put("--", 3);
        PRIORITY.put("+++", 2);
        PRIORITY.put("---", 2);
        PRIORITY.put("+", 1);
        PRIORITY.put("-", 1);
        PRIORITY.put("(", 0);
        buildSufExpr();
    }
    
    private void buildSufExpr() {
        for (String i : operators) {
            switch (i) {
                case "(":
                    stackSuf.push(i);
                    break;
                case "+":
                case "-":
                case "*":
                case "**":
                case "--":
                case "++":
                case "+++":
                case "---":
                case "sin":
                case "cos":
                    moveToStack(i);
                    break;
                case ")":
                    while (!stackSuf.peek().equals("(")) {
                        sufExpr.add(stackSuf.pop());
                    }
                    stackSuf.pop();
                    break;
                default:
                    sufExpr.add(i);
            }
        }
        while (!stackSuf.empty()) {
            sufExpr.add(stackSuf.pop());
        }
    }
    
    public ArrayList<String> getSufExpr() {
        return sufExpr;
    }
    
    private void moveToStack(String i) {
        while (!stackSuf.empty()) {
            if (PRIORITY.get(stackSuf.peek()) >= PRIORITY.get(i)) {
                sufExpr.add(stackSuf.pop());
            } else {
                break;
            }
        }
        stackSuf.push(i);
    }
}
