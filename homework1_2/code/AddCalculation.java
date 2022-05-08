public class AddCalculation implements Calculation {
    public AddCalculation() {
    
    }
    
    @Override
    public Expr calculate(Expr expr1, Expr expr2) {
        // System.out.println("Add!");
        Expr exprNew = new Expr();
        for (Term term : expr1.getTerms()) {
            exprNew.add(term);
        }
        for (Term term : expr2.getTerms()) {
            exprNew.add(term);
        }
        return exprNew;
    }
}
