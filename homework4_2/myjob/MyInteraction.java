package myjob;

import com.oocourse.uml2.interact.common.Pair;
import com.oocourse.uml2.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.models.common.MessageSort;
import com.oocourse.uml2.models.elements.UmlEndpoint;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyInteraction {
    private int participantNum = 0;
    private final UmlInteraction umlInteraction;
    private final Map<String, List<MyLifeLine>> name2lifeLine = new HashMap<>();
    
    public MyInteraction(UmlInteraction umlInteraction) {
        this.umlInteraction = umlInteraction;
    }
    
    public String getName() {
        return umlInteraction.getName();
    }
    
    public void addParticipant(MyLifeLine myLifeLine) {
        participantNum++;
        if (name2lifeLine.containsKey(myLifeLine.getName())) {
            name2lifeLine.get(myLifeLine.getName()).add(myLifeLine);
        } else {
            name2lifeLine.put(myLifeLine.getName(), new ArrayList<MyLifeLine>() {
                {
                    add(myLifeLine);
                }
            });
        }
    }
    
    public void addMessage(UmlMessage umlMessage, Object source, Object target) {
        if (source instanceof UmlEndpoint) {
            ((MyLifeLine) target).increaseFound();
        } else if (target instanceof UmlEndpoint) {
            ((MyLifeLine) source).increaseLost();
        }
        if (source instanceof MyLifeLine &&
                target instanceof MyLifeLine &&
                umlMessage.getMessageSort() == MessageSort.CREATE_MESSAGE) {
            ((MyLifeLine) target).addCreator((MyLifeLine) source);
        }
    }
    
    public int getParticipantNum() {
        return participantNum;
    }
    
    public UmlLifeline getCreator(String name)
            throws LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        if (!name2lifeLine.containsKey(name)) {
            throw new LifelineNotFoundException(umlInteraction.getName(), name);
        } else if (name2lifeLine.get(name).size() > 1) {
            throw new LifelineDuplicatedException(umlInteraction.getName(), name);
        } else {
            MyLifeLine myLifeLine = name2lifeLine.get(name).get(0);
            if (myLifeLine.getCreatorCount() == 0) {
                throw new LifelineNeverCreatedException(umlInteraction.getName(), name);
            } else if (myLifeLine.getCreatorCount() > 1) {
                throw new LifelineCreatedRepeatedlyException(umlInteraction.getName(), name);
            } else {
                return myLifeLine.getCreator().getUmlLifeline();
            }
        }
    }
    
    public Pair<Integer, Integer> getLostAndFound(String name)
            throws LifelineNotFoundException, LifelineDuplicatedException {
        if (!name2lifeLine.containsKey(name)) {
            throw new LifelineNotFoundException(umlInteraction.getName(), name);
        } else if (name2lifeLine.get(name).size() > 1) {
            throw new LifelineDuplicatedException(umlInteraction.getName(), name);
        } else {
            return name2lifeLine.get(name).get(0).getParticipantLostAndFound();
        }
    }
}
