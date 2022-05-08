import com.oocourse.elevator2.PersonRequest;

import java.util.Objects;

public class MyRequest {
    private final PersonRequest personRequest;
    private final int from;
    private final int destination;
    
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
}
