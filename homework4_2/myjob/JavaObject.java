package myjob;

import com.oocourse.uml2.models.elements.UmlAttribute;

public interface JavaObject {
    void addAttribute(UmlAttribute umlAttribute);
    
    void addOperation(MyOperation operation);
}
