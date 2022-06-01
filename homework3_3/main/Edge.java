package main;

import com.oocourse.spec3.main.Person;

public class Edge {
    private final int value;
    private final Person p1;
    private final Person p2;
    
    public Edge(int value, Person p1, Person p2) {
        this.value = value;
        this.p1 = p1;
        this.p2 = p2;
    }
    
    public int getValue() {
        return value;
    }
    
    public Person getP1() {
        return p1;
    }
    
    public Person getP2() {
        return p2;
    }
}
