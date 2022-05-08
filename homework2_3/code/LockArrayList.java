import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockArrayList<T> {
    private final ArrayList<T> list;
    private boolean stop;
    private final ReentrantLock locker;
    private final Condition condition;
    
    public LockArrayList() {
        list = new ArrayList<>();
        stop = false;
        locker = new ReentrantLock();
        condition = locker.newCondition();
    }
    
    public void add(T elem) {
        locker.lock();
        try {
            list.add(elem);
            condition.signalAll();
        } finally {
            locker.unlock();
        }
    }
    
    public T first() {
        locker.lock();
        try {
            if (list.size() > 0) {
                return list.get(0);
            } else {
                return null;
            }
        } finally {
            locker.unlock();
        }
    }
    
    public T pop() {
        locker.lock();
        try {
            if (list.size() > 0) {
                T tmp = list.get(0);
                list.remove(0);
                return tmp;
            } else {
                return null;
            }
        } finally {
            locker.unlock();
        }
    }
    
    public int size() {
        locker.lock();
        try {
            return list.size();
        } finally {
            locker.unlock();
        }
    }
    
    public void setStop() {
        locker.lock();
        try {
            stop = true;
            condition.signalAll();
        } finally {
            locker.unlock();
        }
    }
    
    public boolean isStop() {
        locker.lock();
        try {
            return stop;
        } finally {
            locker.unlock();
        }
    }
    
    public void await() {
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isEmpty() {
        locker.lock();
        try {
            return list.isEmpty();
        } finally {
            locker.unlock();
        }
    }
    
    public void signalAll() {
        //System.out.println(Thread.currentThread().getName());
        condition.signalAll();
    }
    
    public void lock() {
        locker.lock();
    }
    
    public void unlock() {
        locker.unlock();
    }
    
    public void removeRequest(T t) {
        locker.lock();
        try {
            list.remove(t);
        } finally {
            locker.unlock();
        }
    }
    
    public T get(int i) {
        locker.lock();
        try {
            return list.get(i);
        } finally {
            locker.unlock();
        }
    }
}
