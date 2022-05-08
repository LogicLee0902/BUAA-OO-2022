import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.Request;

import java.io.IOException;

public class Input implements Runnable {
    private final LockArrayList<Request> requests;
    
    public Input(LockArrayList<Request> requests) {
        this.requests = requests;
    }
    
    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            requests.lock();
            try {
                if (request == null) {
                    requests.setStop();
                    requests.signalAll();
                    break;
                } else {
                    requests.add(request);
                    requests.signalAll();
                }
            } finally {
                requests.unlock();
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
