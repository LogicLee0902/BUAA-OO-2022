package myjob;

import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml3.models.elements.UmlRegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MyRegion {
    private final UmlRegion umlRegion;
    private MyStateMachine belong;
    private final Map<String, List<MyState>> name2state = new HashMap<>();
    private final List<MyState> states = new ArrayList<>();
    private final List<MyState> finalStates = new ArrayList<>();
    private int initialState;
    private int hasFinal = 0;
    private int canArrived = 0;
    
    public MyRegion(UmlRegion umlRegion) {
        this.umlRegion = umlRegion;
    }
    
    public String getName() {
        return umlRegion.getName();
    }
    
    public void addState(MyState myState) {
        states.add(myState);
        if (myState.getName() != null) {
            if (name2state.containsKey(myState.getName())) {
                name2state.get(myState.getName()).add(myState);
            } else {
                name2state.put(myState.getName(), new ArrayList<MyState>() {
                    {
                        add(myState);
                    }
                });
            }
        }
        if (myState.getType() == 1) {
            initialState = states.indexOf(myState);
        } else if (myState.getType() == 2) {
            hasFinal++;
            finalStates.add(myState);
        }
    }
    
    public int getStatesNum() {
        return states.size();
    }
    
    public void setBelong(MyStateMachine belong) {
        this.belong = belong;
    }
    
    public boolean isCritical(String stateName)
            throws StateNotFoundException, StateDuplicatedException {
        if (!name2state.containsKey(stateName)) {
            throw new StateNotFoundException(belong.getName(), stateName);
        } else if (name2state.get(stateName).size() > 1) {
            throw new StateDuplicatedException(belong.getName(), stateName);
        }
        if (canArrived == 0) {
            if (hasFinal == 0) {
                canArrived = 2;
                return false;
            }
            Set<MyState> visited = new HashSet<>();
            Queue<MyState> queue = new LinkedList<>();
            queue.offer(states.get(initialState));
            while (!queue.isEmpty()) {
                MyState state = queue.poll();
                if (state.getType() == 2) {
                    canArrived = 1;
                    break;
                }
                visited.add(state);
                for (MyState s : state.getSuccessorState()) {
                    if (!visited.contains(s)) {
                        queue.offer(s);
                    }
                }
            }
            if (canArrived == 0) {
                canArrived = 2;
                return false;
            }
        } else if (canArrived == 2) {
            return false;
        }
        MyState myState = name2state.get(stateName).get(0);
        if (myState.getCrucial() == 1) {
            return true;
        } else if (myState.getCrucial() == 2) {
            return false;
        }
        boolean answer = judge(myState);
        if (answer) {
            myState.setCrucial(1);
        } else {
            myState.setCrucial(2);
        }
        return answer;
    }
    
    private boolean judge(MyState myState) {
        if (hasFinal == 0) {
            return false;
        }
        if (myState.getType() != 0) {
            return false;
        }
        MyState start = states.get(initialState);
        Set<MyState> visited = new HashSet<>();
        visited.add(myState);
        Queue<MyState> queue = new LinkedList<>();
        queue.offer(start);
        while (!queue.isEmpty()) {
            MyState state = queue.poll();
            if (state.getType() == 2) {
                return false;
            }
            visited.add(state);
            for (MyState s : state.getSuccessorState()) {
                if (!visited.contains(s)) {
                    queue.offer(s);
                }
            }
        }
        return true;
    }
    
    public List<String> getTransitionsTrigger(String source, String target)
            throws StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        List<String> events = new ArrayList<>();
        if (!name2state.containsKey(source)) {
            throw new StateNotFoundException(belong.getName(), source);
        } else if (name2state.get(source).size() > 1) {
            throw new StateDuplicatedException(belong.getName(), source);
        }
        if (!name2state.containsKey(target)) {
            throw new StateNotFoundException(belong.getName(), target);
        } else if (name2state.get(target).size() > 1) {
            throw new StateDuplicatedException(belong.getName(), target);
        }
        MyState sourceState = name2state.get(source).get(0);
        MyState targetState = name2state.get(target).get(0);
        boolean flag = false;
        for (MyState state : sourceState.getSuccessorState()) {
            if (state.equals(targetState)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new TransitionNotFoundException(belong.getName(), source, target);
        }
        for (MyTransition transition : sourceState.getOutTransitions()) {
            if (transition.getTarget().equals(targetState)) {
                events.addAll(transition.getEvents());
            }
        }
        return events;
    }
    
    public void checkFinalState() throws UmlRule008Exception {
        for (MyState finalState : finalStates) {
            if (finalState.getOutTransitions().size() != 0) {
                throw new UmlRule008Exception();
            }
        }
    }
    
    public void checkState() throws UmlRule009Exception {
        for (MyState myState : states) {
            myState.checkSameTriggerAndGuard();
        }
    }
}
