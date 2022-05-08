// 需要先将官方包中用到的工具类import进来

import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;

import java.util.HashMap;
import java.util.List;

public class Main {
    public static String moveBlankSpace(String str) {
        StringBuilder sb = new StringBuilder();
        for (String i : str.split("[ \\t]+")) {
            sb.append(i);
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
        // 实例化一个ExprInput类型的对象scanner
        // 由于是一般读入模式，所以我们实例化时传递的参数为ExprInputMode.NormalMode
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        
        // 获取自定义函数个数
        int cnt = scanner.getCount();
        
        // 读入自定义函数
        HashMap<Character, FunctionFactor> funcs = new HashMap<>();
        for (int i = 0; i < cnt; i++) {
            String func = scanner.readLine();
            FunctionFactor funcFactor = new FunctionFactor(moveBlankSpace(func));
            funcs.put(func.charAt(0), funcFactor);
            // 存储或者解析逻辑
        }
        // 读入最后一行表达式
        String expr = scanner.readLine();
        Restore restore = new Restore(moveBlankSpace(expr), funcs);
        String str = restore.show();
        // System.out.println("Expand : " + str);
        Split split = new Split(str);
        // System.out.println(split.getOperators());
        SufExpr sufExpr = new SufExpr(split);
        // System.out.println(sufExpr.getSufExpr());
        Parse parse = new Parse(sufExpr);
        int n = parse.getN();
        SolveExpr solveExpr = new SolveExpr(n);
        List<String> exprs = parse.getExprs();
        for (int i = 0; i < n; ++i) {
            String curExpr = exprs.get(i);
            solveExpr.add(new Abstract(curExpr));
        }
        System.out.println(solveExpr.result());
    }
}