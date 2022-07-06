package myjob;

import com.oocourse.uml2.interact.common.Pair;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.format.UserApi;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEndpoint;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyImplementation implements UserApi {
    private final HashMap<String, Object> id2elements = new HashMap<>();
    private final HashMap<String, List<MyClass>> name2Classes = new HashMap<>();
    private final HashMap<String, List<MyInteraction>> name2Interactions = new HashMap<>();
    private final HashMap<String, List<MyStateMachine>> name2StateMachine = new HashMap<>();
    private int count = 0;
    
    public MyImplementation(UmlElement... elements) {
        // CLass, Interface
        // AssociationEnd, Attribute, Generalization,InterfaceRealization, Operation
        // association, params
        for (UmlElement element : elements) {
            if (element instanceof UmlClass) {
                count++;
                parseClass((UmlClass) element);
            } else if (element instanceof UmlInterface) {
                MyInterface myInterface = new MyInterface((UmlInterface) element);
                id2elements.put(element.getId(), myInterface);
            }
        }
        for (UmlElement element : elements) {
            if (element instanceof UmlAttribute) {
                Object parent = id2elements.get(element.getParentId());
                if (parent instanceof JavaObject) {
                    ((JavaObject) parent).addAttribute((UmlAttribute) element);
                }
            } else if (element instanceof UmlOperation) {
                parseOperation((UmlOperation) element);
            } else if (element instanceof UmlGeneralization) {
                parseGeneralization((UmlGeneralization) element);
            } else if (element instanceof UmlInterfaceRealization) {
                parseInterfaceRealization((UmlInterfaceRealization) element);
            } else if (element instanceof UmlStateMachine) {
                parseStateMachine((UmlStateMachine) element);
            } else if (element instanceof UmlInteraction) {
                parseInteraction((UmlInteraction) element);
            }
        }
        
        for (UmlElement element : elements) {
            if (element instanceof UmlParameter) {
                MyOperation myOperation = (MyOperation) id2elements.get(element.getParentId());
                myOperation.addParameter((UmlParameter) element);
            } else if (element instanceof UmlRegion) {
                parseRegion((UmlRegion) element);
            } else if (element instanceof UmlLifeline || element instanceof UmlEndpoint) {
                parseLifeline(element);
            }
        }
        
        for (UmlElement element : elements) {
            if ((element instanceof UmlState) || (element instanceof UmlFinalState)
                    || (element instanceof UmlPseudostate)) {
                parseState(element);
            } else if (element instanceof UmlMessage) {
                parseMessage((UmlMessage) element);
            }
        }
        for (UmlElement e : elements) {
            if (e instanceof UmlTransition) {
                parseTransition((UmlTransition) e);
            }
        }
        
        for (UmlElement element : elements) {
            if (element instanceof UmlEvent) {
                parseEvent((UmlEvent) element);
            }
        }
    }
    
    private void parseClass(UmlClass umlClass) {
        MyClass myClass = new MyClass(umlClass);
        id2elements.put(umlClass.getId(), myClass);
        if (name2Classes.containsKey(umlClass.getName())) {
            name2Classes.get(umlClass.getName()).add(myClass);
        } else {
            List<MyClass> temp = new ArrayList<>();
            temp.add(myClass);
            name2Classes.put(umlClass.getName(), temp);
        }
        
    }
    
    private void parseGeneralization(UmlGeneralization generalization) {
        Object source = id2elements.get(generalization.getSource());
        Object target = id2elements.get(generalization.getTarget());
        if (source instanceof MyClass && target instanceof MyClass) {
            ((MyClass) source).setSuperClass((MyClass) target);
        } else if (source instanceof MyInterface && target instanceof MyInterface) {
            ((MyInterface) source).setSuperInterface((MyInterface) target);
        }
    }
    
    private void parseInterfaceRealization(UmlInterfaceRealization uml) {
        Object source = id2elements.get(uml.getSource());
        Object target = id2elements.get(uml.getTarget());
        if (source instanceof MyClass && target instanceof MyInterface) {
            ((MyClass) source).getInterfaces().add((MyInterface) target);
        }
    }
    
    private void parseOperation(UmlOperation umlOperation) {
        MyOperation myOperation = new MyOperation(umlOperation);
        JavaObject parent = (JavaObject) id2elements.get(umlOperation.getParentId());
        parent.addOperation(myOperation);
        id2elements.put(umlOperation.getId(), myOperation);
    }
    
    private void parseStateMachine(UmlStateMachine umlStateMachine) {
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
    
    private void parseRegion(UmlRegion umlRegion) {
        MyStateMachine myStateMachine = (MyStateMachine) id2elements.get(umlRegion.getParentId());
        MyRegion myRegion = new MyRegion(umlRegion);
        myRegion.setBelong(myStateMachine);
        myStateMachine.setRegion(myRegion);
        id2elements.put(umlRegion.getId(), myRegion);
    }
    
    private void parseState(UmlElement umlState) {
        MyRegion myRegion = (MyRegion) id2elements.get(umlState.getParentId());
        MyState myState = new MyState(umlState);
        myRegion.addState(myState);
        id2elements.put(umlState.getId(), myState);
    }
    
    private void parseTransition(UmlTransition umlTransition) {
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
    
    private void parseEvent(UmlEvent umlEvent) {
        MyTransition myTransition = (MyTransition) id2elements.get(umlEvent.getParentId());
        myTransition.addEvent(umlEvent);
    }
    
    private void parseInteraction(UmlInteraction umlInteraction) {
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
    
    private void parseLifeline(UmlElement umlLifeline) {
        if (umlLifeline instanceof UmlEndpoint) {
            id2elements.put(umlLifeline.getId(), umlLifeline);
            return;
        }
        MyLifeLine myLifeLine = new MyLifeLine((UmlLifeline) umlLifeline);
        id2elements.put(umlLifeline.getId(), myLifeLine);
        MyInteraction myInteraction = (MyInteraction) id2elements.get(umlLifeline.getParentId());
        myInteraction.addParticipant(myLifeLine);
    }
    
    private void parseMessage(UmlMessage umlMessage) {
        Object source = id2elements.get(umlMessage.getSource());
        Object target = id2elements.get(umlMessage.getTarget());
        MyInteraction myInteraction = (MyInteraction) id2elements.get(umlMessage.getParentId());
        myInteraction.addMessage(umlMessage, source, target);
    }
    
    @Override
    public int getClassCount() {
        return count;
    }
    
    @Override
    public int getClassSubClassCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).getSumOfSub();
    }
    
    @Override
    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).getOperationsSize();
    }
    
    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).getOperationVisibility(methodName);
    }
    
    @Override
    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        List<Integer> result = new ArrayList<>();
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        MyClass myClass = name2Classes.get(className).get(0);
        List<MyOperation> operations = new ArrayList<>();
        for (MyOperation myOperation : myClass.getOperations()) {
            if (!myOperation.getName().equals(methodName)) {
                continue;
            }
            if (myOperation.isErrorType()) {
                throw new MethodWrongTypeException(myClass.getClassName(), methodName);
            }
            if (operations.contains(myOperation)) {
                throw new MethodDuplicatedException(className, methodName);
            }
            operations.add(myOperation);
            result.add(myOperation.calculateCouplingDegree(myClass.getId()));
        }
        return result;
    }
    
    @Override
    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).calculateCouplingDegree();
    }
    
    @Override
    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).getInterfacesList();
    }
    
    @Override
    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).getDepth();
    }
    
    @Override
    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        if (!name2Interactions.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (name2Interactions.get(interactionName).size() > 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
        return name2Interactions.get(interactionName).get(0).getParticipantNum();
    }
    
    @Override
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
    
    @Override
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
    
    @Override
    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!name2StateMachine.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        } else if (name2StateMachine.get(stateMachineName).size() > 1) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        return name2StateMachine.get(stateMachineName).get(0).getMyRegion().getStatesNum();
    }
    
    @Override
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
    
    @Override
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
}
