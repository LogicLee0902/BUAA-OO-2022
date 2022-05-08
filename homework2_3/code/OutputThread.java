import com.oocourse.TimableOutput;

public class OutputThread {
    
    private static final OutputThread OUTPUT_THREAD = new OutputThread();
    
    private OutputThread(){}
    
    public static OutputThread getInstance() {
        return OUTPUT_THREAD;
    }
    
    public synchronized void println(String msg) {
        TimableOutput.println(msg);
    }
}