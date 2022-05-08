import com.oocourse.elevator3.PersonRequest;

import java.util.Objects;

public class MyRequest {
    private final PersonRequest personRequest;
    private int from;
    private int destination;
    private int tranFloor = 1;
    private char tranBuilding = 'A';
    
    public MyRequest(PersonRequest personRequest) {
        this.personRequest = personRequest;
        if (personRequest.getFromBuilding() != personRequest.getToBuilding()) {
            from = personRequest.getFromBuilding() - 'A' + 1;
            destination = personRequest.getToBuilding() - 'A' + 1;
        } else {
            from = personRequest.getFromFloor();
            destination = personRequest.getToFloor();
        }
    }
    
    public char getFromBuilding() {
        return personRequest.getFromBuilding();
    }
    
    public char getToBuilding() {
        return personRequest.getToBuilding();
    }
    
    public int getFromFloor() {
        return personRequest.getFromFloor();
    }
    
    public int getToFloor() {
        return personRequest.getToFloor();
    }
    
    public int getPersonId() {
        return personRequest.getPersonId();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyRequest)) {
            return false;
        }
        MyRequest myRequest = (MyRequest) o;
        return from == myRequest.from
                && destination == myRequest.destination
                && personRequest.equals(myRequest.personRequest);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(personRequest, from, destination);
    }
    
    public int getFrom() {
        return from;
    }
    
    public int getDestination() {
        return destination;
    }
    
    public boolean isArrive() {
        return tranBuilding == this.getToBuilding() && tranFloor == this.getToFloor();
    }
    
    public void update(int high, int current) {
        if (high == 5) {
            this.tranBuilding = (char) ('A' + current - 1);
            this.tranFloor = this.getFromFloor();
        } else {
            this.tranFloor = current;
            this.tranBuilding = this.getFromBuilding();
        }
    }
    
    public int getTranFloor() {
        return tranFloor;
    }
    
    public char getTranBuilding() {
        return tranBuilding;
    }
    
    public void setDestination(int destination) {
        this.destination = destination;
    }
    
    public void setFrom(int from) {
        this.from = from;
    }
    
    public String toString() {
        return personRequest.toString();
    }
}
