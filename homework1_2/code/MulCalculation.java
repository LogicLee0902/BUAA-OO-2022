public class MulCalculation implements Calculation {
    public MulCalculation() {
    
    }
    
    @Override
    public Expr calculate(Expr expr1, Expr expr2) {
        // System.out.println("Mul!");
        Expr exprNew = new Expr();
        for (Term i : expr1.getTerms()) {
            for (Term j : expr2.getTerms()) {
                exprNew.add(exprNew.merge(i, j));
            }
        }
        return exprNew;
    }
}
