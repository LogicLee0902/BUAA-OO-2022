package myjob;

import com.oocourse.uml2.interact.common.Pair;
import com.oocourse.uml2.models.elements.UmlLifeline;

import java.util.ArrayList;
import java.util.List;

public class MyLifeLine {
    private final UmlLifeline umlLifeline;
    private int numberOfFound = 0;
    private int numberOfLost = 0;
    private final List<MyLifeLine> creators = new ArrayList<>();
    
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
}
