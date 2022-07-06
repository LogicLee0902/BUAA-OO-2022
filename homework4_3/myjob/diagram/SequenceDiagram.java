package myjob.diagram;

import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;
import myjob.MyInteraction;
import myjob.MyLifeLine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SequenceDiagram {
    private final Map<String, Object> id2elements;
    private final HashMap<String, List<MyInteraction>> name2Interactions = new HashMap<>();
    private final List<UmlAttribute> attributes = new ArrayList<>();
    
    public SequenceDiagram(Map<String, Object> id2elements) {
        this.id2elements = id2elements;
    }
    
    public void parseInteraction(UmlInteraction umlInteraction) {
        MyInteraction myInteraction = new MyInteraction(umlInteraction);
        id2elements.put(umlInteraction.getId(), myInteraction);
        if (name2Interactions.containsKey(umlInteraction.getName())) {
            name2Interactions.get(umlInteraction.getName()).add(myInteraction);
        } else {
            name2Interactions.put(umlInteraction.getName(), new ArrayList<MyInteraction>() {
                {
                    add(myInteraction);
                }
            });
        }
    }
    
    public void parseLifeline(UmlElement umlLifeline) {
        if (umlLifeline instanceof UmlEndpoint) {
            id2elements.put(umlLifeline.getId(), umlLifeline);
            return;
        }
        MyLifeLine myLifeLine = new MyLifeLine((UmlLifeline) umlLifeline);
        id2elements.put(umlLifeline.getId(), myLifeLine);
        MyInteraction myInteraction = (MyInteraction) id2elements.get(umlLifeline.getParentId());
        myInteraction.addParticipant(myLifeLine);
    }
    
    public void parseMessage(UmlMessage umlMessage) {
        Object source = id2elements.get(umlMessage.getSource());
        Object target = id2elements.get(umlMessage.getTarget());
        MyInteraction myInteraction = (MyInteraction) id2elements.get(umlMessage.getParentId());
        myInteraction.addMessage(umlMessage, source, target);
    }
    
    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        if (!name2Interactions.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (name2Interactions.get(interactionName).size() > 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
        return name2Interactions.get(interactionName).get(0).getParticipantNum();
    }
    
    public UmlLifeline getParticipantCreator(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        if (!name2Interactions.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (name2Interactions.get(interactionName).size() > 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
        return name2Interactions.get(interactionName).get(0).getCreator(lifelineName);
    }
    
    public Pair<Integer, Integer> getParticipantLostAndFound(
            String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        if (!name2Interactions.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (name2Interactions.get(interactionName).size() > 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
        return name2Interactions.get(interactionName).get(0).getLostAndFound(lifelineName);
    }
    
    public void checkForUml006() throws UmlRule006Exception {
        for (String name : name2Interactions.keySet()) {
            for (MyInteraction myInteraction : name2Interactions.get(name)) {
                myInteraction.checkAttribute(id2elements);
            }
        }
    }
    
    public void checkForUml007() throws UmlRule007Exception {
        for (String name : name2Interactions.keySet()) {
            for (MyInteraction myInteraction : name2Interactions.get(name)) {
                myInteraction.checkDelete();
            }
        }
    }
}
