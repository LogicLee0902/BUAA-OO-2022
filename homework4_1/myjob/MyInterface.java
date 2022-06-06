package myjob;

import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlInterface;

import java.util.ArrayList;
import java.util.List;

public class MyInterface implements JavaObject {
    private final List<MyInterface> superInterface = new ArrayList<>();
    private final ArrayList<UmlAttribute> attributes = new ArrayList<>();
    private final ArrayList<MyOperation> operations = new ArrayList<>();
    private final UmlInterface umlInterface;
    
    public MyInterface(UmlInterface umlInterface) {
        this.umlInterface = umlInterface;
    }
    
    public String getName() {
        return umlInterface.getName();
    }
    
    public void setSuperInterface(MyInterface superInterface) {
        this.superInterface.add(superInterface);
    }
    
    public List<MyInterface> getSuperInterface() {
        return this.superInterface;
    }
    
    @Override
    public void addAttribute(UmlAttribute umlAttribute) {
        attributes.add(umlAttribute);
    }
    
    @Override
    public void addOperation(MyOperation operation) {
        operations.add(operation);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MyInterface)) {
            return false;
        }
        MyInterface myInterface = (MyInterface) obj;
        return umlInterface.equals(myInterface.umlInterface);
    }
    
    @Override
    public int hashCode() {
        return umlInterface.hashCode();
    }
}
