package main;

public class DisToCur {
    private final int id;
    private final int distance;
    
    public DisToCur(int id, int distance) {
        this.id = id;
        this.distance = distance;
    }
    
    public int getId() {
        return id;
    }
    
    public int getDistance() {
        return distance;
    }
}
