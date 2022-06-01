package main;

import com.oocourse.spec3.exceptions.EqualEmojiIdException;

import java.util.HashMap;

public class MyEqualEmojiIdException extends EqualEmojiIdException {
    private final int id;
    private static final Counter COUNTER = new Counter();
    private static final HashMap<Integer, Integer>
            EXCEPT_COUNTER = new HashMap<Integer, Integer>();
    
    public MyEqualEmojiIdException(int id) {
        this.id = id;
        if (!EXCEPT_COUNTER.containsKey(id)) {
            EXCEPT_COUNTER.put(id, 0);
        }
    }
    
    @Override
    public void print() {
        COUNTER.increase();
        EXCEPT_COUNTER.put(id, EXCEPT_COUNTER.get(id) + 1);
        System.out.println("eei-" + COUNTER.getCounter() + ", "
                + id + "-" + EXCEPT_COUNTER.get(id));
    }
}
