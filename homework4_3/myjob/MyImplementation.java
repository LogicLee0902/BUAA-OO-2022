package myjob;

import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml3.interact.format.UserApi;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlState;
import com.oocourse.uml3.models.elements.UmlStateMachine;
import com.oocourse.uml3.models.elements.UmlTransition;
import myjob.diagram.ClassDiagram;
import myjob.diagram.SequenceDiagram;
import myjob.diagram.StateDiagram;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyImplementation implements UserApi {
    private final HashMap<String, Object> id2elements = new HashMap<>();
    private final ClassDiagram classDiagram = new ClassDiagram(id2elements);
    private final StateDiagram stateDiagram = new StateDiagram(id2elements);
    private final SequenceDiagram sequenceDiagram = new SequenceDiagram(id2elements);
    private int count = 0;
    
    public MyImplementation(UmlElement... elements) {
        searchForClassOrInterface(elements);
        for (UmlElement element : elements) {
            if (element instanceof UmlAttribute) {
                parseAttribute((UmlAttribute) element);
            } else if (element instanceof UmlOperation) {
                parseOperation((UmlOperation) element);
            } else if (element instanceof UmlAssociationEnd) {
                parseAssociationEnd((UmlAssociationEnd) element);
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
            if (element instanceof UmlAssociation) {
                parseAssociation((UmlAssociation) element);
            }
            if (element instanceof UmlParameter) {
                parseParameter((UmlParameter) element);
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
    
    private void searchForClassOrInterface(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlClass) {
                count++;
                parseClass((UmlClass) element);
            } else if (element instanceof UmlInterface) {
                parseInterface((UmlInterface) element);
            }
        }
    }
    
    private void parseClass(UmlClass umlClass) {
        classDiagram.parseClass(umlClass);
    }
    
    private void parseInterface(UmlInterface umlInterface) {
        classDiagram.parseInterface(umlInterface);
    }
    
    private void parseGeneralization(UmlGeneralization generalization) {
        classDiagram.parseGeneralization(generalization);
    }
    
    private void parseInterfaceRealization(UmlInterfaceRealization uml) {
        classDiagram.parseInterfaceRealization(uml);
    }
    
    private void parseOperation(UmlOperation umlOperation) {
        classDiagram.parseOperation(umlOperation);
    }
    
    private void parseParameter(UmlParameter umlParameter) {
        classDiagram.parseParameter(umlParameter);
    }
    
    private void parseAttribute(UmlAttribute umlAttribute) {
        classDiagram.parseAttribute(umlAttribute);
    }
    
    private void parseAssociation(UmlAssociation umlAssociation) {
        classDiagram.parseAssociation(umlAssociation);
    }
    
    private void parseAssociationEnd(UmlAssociationEnd umlAssociationEnd) {
        classDiagram.parseAssociationEnd(umlAssociationEnd);
    }
    
    private void parseStateMachine(UmlStateMachine umlStateMachine) {
        stateDiagram.parseStateMachine(umlStateMachine);
    }
    
    private void parseRegion(UmlRegion umlRegion) {
        stateDiagram.parseRegion(umlRegion);
    }
    
    private void parseState(UmlElement umlState) {
        stateDiagram.parseState(umlState);
    }
    
    private void parseTransition(UmlTransition umlTransition) {
        stateDiagram.parseTransition(umlTransition);
    }
    
    private void parseEvent(UmlEvent umlEvent) {
        stateDiagram.parseEvent(umlEvent);
    }
    
    private void parseInteraction(UmlInteraction umlInteraction) {
        sequenceDiagram.parseInteraction(umlInteraction);
    }
    
    private void parseLifeline(UmlElement umlLifeline) {
        sequenceDiagram.parseLifeline(umlLifeline);
    }
    
    private void parseMessage(UmlMessage umlMessage) {
        sequenceDiagram.parseMessage(umlMessage);
    }
    
    @Override
    public int getClassCount() {
        return count;
    }
    
    @Override
    public int getClassSubClassCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassSubClassCount(className);
    }
    
    @Override
    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassOperationCount(className);
    }
    
    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassOperationVisibility(className, methodName);
    }
    
    @Override
    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        return classDiagram.getClassOperationCouplingDegree(className, methodName);
    }
    
    @Override
    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassAttributeCouplingDegree(className);
    }
    
    @Override
    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassImplementInterfaceList(className);
    }
    
    @Override
    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassDepthOfInheritance(className);
    }
    
    @Override
    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        return sequenceDiagram.getParticipantCount(interactionName);
    }
    
    @Override
    public UmlLifeline getParticipantCreator(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        return sequenceDiagram.getParticipantCreator(interactionName, lifelineName);
    }
    
    @Override
    public Pair<Integer, Integer> getParticipantLostAndFound(
            String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return sequenceDiagram.getParticipantLostAndFound(interactionName, lifelineName);
    }
    
    @Override
    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return stateDiagram.getStateCount(stateMachineName);
    }
    
    @Override
    public boolean getStateIsCriticalPoint(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return stateDiagram.getStateIsCriticalPoint(stateMachineName, stateName);
    }
    
    @Override
    public List<String> getTransitionTrigger(
            String stateMachineName, String sourceStateName, String targetStateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        return stateDiagram.getTransitionTrigger(
                stateMachineName, sourceStateName, targetStateName);
    }
    
    @Override
    public void checkForUml001() throws UmlRule001Exception {
        classDiagram.checkForUml001();
    }
    
    @Override
    public void checkForUml002() throws UmlRule002Exception {
        classDiagram.checkForUml002();
    }
    
    @Override
    public void checkForUml003() throws UmlRule003Exception {
        classDiagram.checkForUml003();
    }
    
    @Override
    public void checkForUml004() throws UmlRule004Exception {
        classDiagram.checkForUml004();
    }
    
    @Override
    public void checkForUml005() throws UmlRule005Exception {
        classDiagram.checkForUml005();
    }
    
    @Override
    public void checkForUml006() throws UmlRule006Exception {
        sequenceDiagram.checkForUml006();
    }
    
    @Override
    public void checkForUml007() throws UmlRule007Exception {
        sequenceDiagram.checkForUml007();
    }
    
    @Override
    public void checkForUml008() throws UmlRule008Exception {
        stateDiagram.checkForUml008();
    }
    
    @Override
    public void checkForUml009() throws UmlRule009Exception {
        stateDiagram.checkForUml009();
    }
}
