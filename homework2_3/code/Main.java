import com.oocourse.TimableOutput;
import com.oocourse.elevator3.Request;

public class Main {
    public static void main(String[] args) {
        LockArrayList<Request> requests = new LockArrayList<>();
        TimableOutput.initStartTimestamp();
        Thread input = new Thread(new Input(requests), "Input");
        Thread dispatcher = new Thread(new Dispatcher(requests), "Dispatcher");
        input.start();
        dispatcher.start();
    }
}
