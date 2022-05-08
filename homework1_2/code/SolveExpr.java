import java.math.BigInteger;
import java.util.HashMap;

public class SolveExpr {
    private HashMap<String, Expr> exprs = new HashMap<>();
    private String aim;
    
    public SolveExpr(int n) {
        this.aim = "f" + n;
    }
    
    private Expr build(String operand) {
        Expr exprNew = new Expr();
        if (operand.charAt(0) != 'f') {
            Term termNew;
            if (operand.charAt(0) == 'x') {
                // find x
                termNew = new Term(BigInteger.ONE, BigInteger.ONE);
            } else {
                // find the number
                termNew = new Term(BigInteger.ZERO, new BigInteger(operand));
            }
            exprNew.add(termNew);
        } else {
            exprNew.setTerms(exprs.get(operand).getTerms());
        }
        return exprNew;
    }
    
    public void add(Abstract elem) {
        String id = elem.getId();
        String op = elem.getOp();
        String operand1 = elem.getFactor();
        String operand2 = elem.getAim();
        Expr exprNew = new Expr();
        if (op.equals("null")) {
            exprNew = build(operand1);
            
        } else if (op.equals("pos") || op.equals("neg")
                || op.equals("sin") || op.equals("cos")) {
            exprNew = build(operand1);
            if (op.equals("neg")) {
                exprNew.neg();
            } else if (op.equals("sin") || op.equals("cos")) {
                exprNew.triangulation(op);
            }
        } else {
            Expr expr1 = build(operand1);
            Expr expr2 = build(operand2);
            Calculation cal;
            switch (op) {
                case "add":
                    cal = new AddCalculation();
                    break;
                case "sub":
                    expr2.neg();
                    cal = new AddCalculation();
                    break;
                case "pow":
                case "mul":
                    cal = new MulCalculation();
                    break;
                default:
                    cal = null;
            }
            if (!op.equals("pow")) {
                exprNew = cal.calculate(expr1, expr2);
            } else {
                int pow = Integer.parseInt(operand2);
                exprNew.add(new Term(BigInteger.ZERO, BigInteger.ONE));
                while (pow > 0) {
                    exprNew = cal.calculate(exprNew, expr1);
                    pow--;
                }
            }
        }
        exprNew.sort();
        // System.out.println("Term is  the x ** " + exprNew.getTerms());
        // System.out.println(exprNew);
        exprs.put(id, exprNew);
    }
    
    public String result() {
        Expr expr = exprs.get(this.aim);
        for (Term i : expr.getTerms()) {
            i.simplify();
        }
        expr.simplify();
        return expr.toString();
    }
}
