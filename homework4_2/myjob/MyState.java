package myjob;

import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlPseudostate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
