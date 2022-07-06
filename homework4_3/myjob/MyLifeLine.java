package myjob;

import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.models.elements.UmlLifeline;

import java.util.ArrayList;
import java.util.List;

public class MyLifeLine {
    private final UmlLifeline umlLifeline;
    private int numberOfFound = 0;
    private int numberOfLost = 0;
    private final List<MyLifeLine> creators = new ArrayList<>();
    /*
     * 0 means normal
     * 1 means delete
     * >1 means received a message after deleted
     */
    private int state = 0;
    
    public MyLifeLine(UmlLifeline umlLifeline) {
        this.umlLifeline = umlLifeline;
    }
    
    public String getName() {
        return umlLifeline.getName();
    }
    
    public void addCreator(MyLifeLine myLifeline) {
        creators.add(myLifeline);
    }
    
    public int getCreatorCount() {
        return creators.size();
    }
    
    public MyLifeLine getCreator() {
        return creators.get(0);
    }
    
    public void increaseLost() {
        numberOfLost++;
    }
    
    public void increaseFound() {
        numberOfFound++;
    }
    
    public Pair<Integer, Integer> getParticipantLostAndFound() {
        return new Pair<>(numberOfFound, numberOfLost);
    }
    
    public UmlLifeline getUmlLifeline() {
        return umlLifeline;
    }
    
    public int getState() {
        return state;
    }
    
    public String getRepresent() {
        return umlLifeline.getRepresent();
    }
    
    public String getParentId() {
        return umlLifeline.getParentId();
    }
    
    public void changeState() {
        state++;
    }
}
