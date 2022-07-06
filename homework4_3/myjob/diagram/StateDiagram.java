package myjob.diagram;

import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlStateMachine;
import com.oocourse.uml3.models.elements.UmlTransition;
import myjob.MyRegion;
import myjob.MyState;
import myjob.MyStateMachine;
import myjob.MyTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateDiagram {
    private final Map<String, List<MyStateMachine>> name2StateMachine = new HashMap<>();
    private final Map<String, Object> id2elements;
    
    public StateDiagram(Map<String, Object> id2elements) {
        this.id2elements = id2elements;
    }
    
    public void parseStateMachine(UmlStateMachine umlStateMachine) {
        MyStateMachine myStateMachine = new MyStateMachine(umlStateMachine);
        id2elements.put(umlStateMachine.getId(), myStateMachine);
        if (name2StateMachine.containsKey(umlStateMachine.getName())) {
            name2StateMachine.get(myStateMachine.getName()).add(myStateMachine);
        } else {
            name2StateMachine.put(umlStateMachine.getName(), new ArrayList<MyStateMachine>() {
                {
                    add(myStateMachine);
                }
            });
        }
    }
    
    public void parseRegion(UmlRegion umlRegion) {
        MyStateMachine myStateMachine = (MyStateMachine) id2elements.get(umlRegion.getParentId());
        MyRegion myRegion = new MyRegion(umlRegion);
        myRegion.setBelong(myStateMachine);
        myStateMachine.setRegion(myRegion);
        id2elements.put(umlRegion.getId(), myRegion);
    }
    
    public void parseState(UmlElement umlState) {
        MyRegion myRegion = (MyRegion) id2elements.get(umlState.getParentId());
        MyState myState = new MyState(umlState);
        myRegion.addState(myState);
        id2elements.put(umlState.getId(), myState);
    }
    
    public void parseTransition(UmlTransition umlTransition) {
        MyTransition myTransition = new MyTransition(umlTransition);
        id2elements.put(umlTransition.getId(), myTransition);
        String sourceId = umlTransition.getSource();
        String targetId = umlTransition.getTarget();
        MyState source = (MyState) id2elements.get(sourceId);
        MyState target = (MyState) id2elements.get(targetId);
        myTransition.setTarget(target);
        myTransition.setSource(source);
        source.addTranslation(myTransition);
        source.addSuccessor(target);
    }
    
    public void parseEvent(UmlEvent umlEvent) {
        MyTransition myTransition = (MyTransition) id2elements.get(umlEvent.getParentId());
        myTransition.addEvent(umlEvent);
    }
    
    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!name2StateMachine.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        } else if (name2StateMachine.get(stateMachineName).size() > 1) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        return name2StateMachine.get(stateMachineName).get(0).getMyRegion().getStatesNum();
    }
    
    public boolean getStateIsCriticalPoint(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        if (!name2StateMachine.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        } else if (name2StateMachine.get(stateMachineName).size() > 1) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        return name2StateMachine.get(stateMachineName).get(0).getMyRegion().isCritical(stateName);
    }
    
    public List<String> getTransitionTrigger(
            String stateMachineName, String sourceStateName, String targetStateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        if (!name2StateMachine.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        } else if (name2StateMachine.get(stateMachineName).size() > 1) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        return name2StateMachine.get(stateMachineName).get(0).
                getMyRegion().getTransitionsTrigger(sourceStateName, targetStateName);
    }
    
    public void checkForUml008() throws UmlRule008Exception {
        for (String name : name2StateMachine.keySet()) {
            for (MyStateMachine myStateMachine : name2StateMachine.get(name)) {
                myStateMachine.checkForUml008();
            }
        }
    }
    
    public void checkForUml009() throws UmlRule009Exception {
        for (String name : name2StateMachine.keySet()) {
            for (MyStateMachine myStateMachine : name2StateMachine.get(name)) {
                myStateMachine.checkFormUml009();
            }
        }
    }
}
