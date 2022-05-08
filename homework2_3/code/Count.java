import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Count {
    private static int count;
    private final ReentrantLock locker;
    private final Condition condition;
    private static final Count COUNT = new Count();
    
    public Count() {
        count = 0;
        locker = new ReentrantLock();
        condition = locker.newCondition();
    }
    
    public static Count getInstance() {
        return COUNT;
    }
    
    public void increase() {
        locker.lock();
        try {
            count++;
            condition.signalAll();
        } finally {
            locker.unlock();
        }
    }
    
    public void decrease() {
        locker.lock();
        try {
            while (true) {
                if (count > 0) {
                    count--;
                    break;
                } else {
                    condition.await();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            locker.unlock();
        }
    }
    
    public boolean isCleared() {
        locker.lock();
        try {
            // System.out.println(count);
            return count == 0;
        } finally {
            locker.unlock();
        }
    }
}
