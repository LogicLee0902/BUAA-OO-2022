package main;

public class Counter {
    private  int counter;
    
    public Counter() {
        counter = 0;
    }
    
    public void increase() {
        counter += 1;
    }
    
    public int getCounter() {
        return counter;
    }
}
