package myjob;

import com.oocourse.uml2.models.elements.UmlStateMachine;

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
}
