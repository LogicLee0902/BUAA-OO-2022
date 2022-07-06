package myjob;

import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.List;

public class MyTransition {
    private final UmlTransition umlTransition;
    private final List<UmlEvent> events = new ArrayList<>();
    private MyState source;
    private MyState target;
    
    public MyTransition(UmlTransition umlTransition) {
        this.umlTransition = umlTransition;
    }
    
    public void addEvent(UmlEvent event) {
        events.add(event);
    }
    
    public List<String> getEvents() {
        List<String> eventNames = new ArrayList<>();
        for (UmlEvent event : events) {
            eventNames.add(event.getName());
        }
        return eventNames;
    }
    
    public void setSource(MyState source) {
        this.source = source;
    }
    
    public void setTarget(MyState target) {
        this.target = target;
    }
    
    public MyState getSource() {
        return source;
    }
    
    public MyState getTarget() {
        return target;
    }
    
    public String getId() {
        return umlTransition.getId();
    }
    
    public String getGuard() {
        return umlTransition.getGuard();
    }
}
