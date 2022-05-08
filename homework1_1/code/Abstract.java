public class Abstract {
    
    private String id;
    private String op;
    private String aim;
    private String factor;
    // means <exp, coeff>
    
    public Abstract(String expr) {
        this.parseMode(expr);
    }
    
    public void parseMode(String expr) {
        String[] strs = expr.split("[ ]+");
        this.id = strs[0];
        switch (strs.length) {
            case 2:
                factor = strs[1];
                op = "null";
                aim = "null";
                break;
            case 3:
                op = strs[1];
                factor = strs[2];
                break;
            case 4:
                op = strs[1];
                factor = strs[2];
                aim = strs[3];
                break;
            default:
        }
        
    }
    
    public String getId() {
        return id;
    }
    
    public String getOp() {
        return op;
    }
    
    public String getAim() {
        return aim;
    }
    
    public String getFactor() {
        return factor;
    }
}
