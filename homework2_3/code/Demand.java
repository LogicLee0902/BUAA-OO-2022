import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Demand<T> {
    private final ConcurrentHashMap<Integer, CopyOnWriteArrayList<T>> queue;
    private final ReentrantLock locker;
    
    public Demand() {
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
    
    public void remove(int t) {
        locker.lock();
        try {
            queue.remove(t);
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
                CopyOnWriteArrayList<T> tmp = new CopyOnWriteArrayList<>();
                tmp.add(person);
                queue.put(t, tmp);
            }
        } finally {
            locker.unlock();
        }
    }
    
    public CopyOnWriteArrayList<T> get(int t) {
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
    
    public boolean isEmpty() {
        locker.lock();
        try {
            return queue.isEmpty();
        } finally {
            locker.unlock();
        }
    }
}
