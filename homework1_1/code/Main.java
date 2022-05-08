// 需要先将官方包中用到的工具类import进来

import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 实例化一个ExprInput类型的对象scanner
        // 由于是一般读入模式，所以我们实例化时传递的参数为ExprInputMode.NormalMode
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        //  Scanner scanner = new Scanner(System.in);
        // 一般读入模式下，读入一行字符串时使用readLine()方法，在这里我们使用其读入表达式
        //String expr = scanner.nextLine();
        String expr = scanner.readLine();
        // 表达式括号展开相关的逻辑
        StringBuilder sb = new StringBuilder();
        for (String i : expr.split("[ \\t]+")) {
            sb.append(i);
        }
        Split split = new Split(sb.toString());
        SufExpr sufExpr = new SufExpr(split);
        Parse parse = new Parse(sufExpr);
        int n = parse.getN();
        List<String> exprs = parse.getExprs();
        // System.out.println(n);
        // for (int i = 0; i < n; ++i) {
        //    String curExpr = exprs.get(i);
        //    System.out.println(curExpr);
        // }
        Poly poly = new Poly(n);
        for (String line : exprs) {
            Abstract parser = new Abstract(line);
            poly.add(parser);
        }
        System.out.println(poly.toString());
    }
}