package myjob;

import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlPseudostate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyState {
    private final UmlElement umlState;
    private final List<MyTransition> outTransitions = new ArrayList<>();
    private final Set<MyState> successorState = new HashSet<>();
    /*
     * 0 for normal state
     * 1 for pseudo state
     * 2 for final state
     */
    private final int type;
    
    /*
     * 0 for not checked;
     * 1 for crucial
     * 2 for not crucial
     */
    private int crucial = 0;
    
    public MyState(UmlElement umlState) {
        if (umlState instanceof UmlPseudostate) {
            type = 1;
        } else if (umlState instanceof UmlFinalState) {
            type = 2;
        } else {
            type = 0;
        }
        this.umlState = umlState;
    }
    
    public int getType() {
        return type;
    }
    
    public String getName() {
        return umlState.getName();
    }
    
    public void addSuccessor(MyState myState) {
        successorState.add(myState);
    }
    
    public Set<MyState> getSuccessorState() {
        return successorState;
    }
    
    public void setCrucial(int crucial) {
        this.crucial = crucial;
    }
    
    public int getCrucial() {
        return crucial;
    }
    
    public void addTranslation(MyTransition myTransition) {
        outTransitions.add(myTransition);
    }
    
    public List<MyTransition> getOutTransitions() {
        return outTransitions;
    }
    
    public void     checkSameTriggerAndGuard() throws UmlRule009Exception {
        Map<String, List<MyTransition>> trigger2transition = new HashMap<>();
        for (MyTransition myTransition : outTransitions) {
            List<String> names = myTransition.getEvents();
            for (String name : names) {
                if (!trigger2transition.containsKey(name)) {
                    trigger2transition.put(name, new ArrayList<MyTransition>() {
                        {
                            add(myTransition);
                        }
                    });
                } else {
                    for (MyTransition transition : trigger2transition.get(name)) {
                        if (transition.getGuard() != null && !transition.getGuard().trim().isEmpty()
                                && myTransition.getGuard() != null &&
                                !myTransition.getGuard().trim().isEmpty()
                                && !transition.getGuard().equals(myTransition.getGuard())) {
                            continue;
                        }
                        throw new UmlRule009Exception();
                    }
                    trigger2transition.get(name).add(myTransition);
                }
            }
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MyState)) {
            return false;
        }
        MyState other = (MyState) obj;
        return this.umlState.getId().equals(other.umlState.getId());
    }
}
