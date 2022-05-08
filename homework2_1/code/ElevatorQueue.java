import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ElevatorQueue<T> {
    private final ConcurrentHashMap<Integer, LockArrayList<T>> queue;
    private final ReentrantLock locker;
    
    public ElevatorQueue() {
        queue = new ConcurrentHashMap<>();
        locker = new ReentrantLock();
    }
    
    public int size() {
        locker.lock();
        try {
            return queue.keySet().size();
        } finally {
            locker.unlock();
        }
    }
    
    public boolean containsKey(int t) {
        locker.lock();
        try {
            return queue.containsKey(t);
        } finally {
            locker.unlock();
        }
    }
    
    public void add(int t, T person) {
        locker.lock();
        try {
            if (queue.containsKey(t)) {
                queue.get(t).add(person);
            } else {
                LockArrayList<T> tmp = new LockArrayList<>();
                tmp.add(person);
                queue.put(t, tmp);
            }
        } finally {
            locker.unlock();
        }
    }
    
    public LockArrayList<T> get(int t) {
        locker.lock();
        try {
            return queue.get(t);
        } finally {
            locker.unlock();
        }
    }
    
    public void lock() {
        locker.lock();
    }
    
    public void unlock() {
        locker.unlock();
    }
}