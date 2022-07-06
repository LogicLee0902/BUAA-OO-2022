package myjob;

import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml3.models.elements.UmlStateMachine;

public class MyStateMachine {
    private final UmlStateMachine umlStateMachine;
    
    private MyRegion myRegion;
    
    public MyStateMachine(UmlStateMachine umlStateMachine) {
        this.umlStateMachine = umlStateMachine;
    }
    
    public String getName() {
        return umlStateMachine.getName();
    }
    
    public void setRegion(MyRegion myRegion) {
        this.myRegion = myRegion;
    }
    
    public MyRegion getMyRegion() {
        return myRegion;
    }
    
    public void checkForUml008() throws UmlRule008Exception {
        myRegion.checkFinalState();
    }
    
    public void checkFormUml009() throws UmlRule009Exception {
        myRegion.checkState();
    }
}
