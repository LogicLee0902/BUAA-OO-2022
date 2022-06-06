package myjob;

import com.oocourse.uml1.models.elements.UmlAttribute;

public interface JavaObject {
    void addAttribute(UmlAttribute umlAttribute);
    
    void addOperation(MyOperation operation);
}
