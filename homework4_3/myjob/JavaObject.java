package myjob;

import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;

public interface JavaObject {
    void addAttribute(UmlAttribute umlAttribute);
    
    void addOperation(MyOperation operation);
    
    void addEnds(UmlAssociationEnd end);
}
