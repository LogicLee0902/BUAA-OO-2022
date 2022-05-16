package main;

import com.oocourse.spec2.exceptions.EqualRelationException;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private final int id1;
    private final int id2;
    private static final Counter COUNTER = new Counter();
    private static final HashMap<Integer, Integer> EXCEPT_COUNTER = new HashMap<Integer, Integer>();
    
    public MyEqualRelationException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id2, id1);
        if (!EXCEPT_COUNTER.containsKey(id1)) {
            EXCEPT_COUNTER.put(id1, 0);
        }
        
        if (!EXCEPT_COUNTER.containsKey(id2)) {
            EXCEPT_COUNTER.put(id2, 0);
        }
    }
    
    @Override
    public void print() {
        COUNTER.increase();
        if (id1 == id2) {
            EXCEPT_COUNTER.put(id1, EXCEPT_COUNTER.get(id1) + 1);
        } else {
            EXCEPT_COUNTER.put(id1, EXCEPT_COUNTER.get(id1) + 1);
            EXCEPT_COUNTER.put(id2, EXCEPT_COUNTER.get(id2) + 1);
        }
        System.out.println("er-" + COUNTER.getCounter() + ", " + id1 + "-" + EXCEPT_COUNTER.get(id1)
                + ", " + id2 + "-" + EXCEPT_COUNTER.get(id2));
    }
}
