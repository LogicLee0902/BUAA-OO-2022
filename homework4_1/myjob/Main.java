package myjob;

import com.oocourse.uml1.interact.AppRunner;

public class Main {
    public static void main(String[] args) throws Exception {
        AppRunner appRunner = AppRunner.newInstance(MyImplementation.class);
        appRunner.run(args);
    }
}
