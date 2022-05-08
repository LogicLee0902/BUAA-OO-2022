import com.oocourse.TimableOutput;
import com.oocourse.elevator2.Request;

public class Main {
    public static void main(String[] args) {
        LockArrayList<Request> requests = new LockArrayList<>();
        TimableOutput.initStartTimestamp();
        Thread input = new Thread(new Input(requests), "Input");
        Thread dispatcher = new Thread(new Dispatcher(requests), "Input");
        input.start();
        dispatcher.start();
    }
}
