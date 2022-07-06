package myjob;

import com.oocourse.uml2.models.common.NamedType;
import com.oocourse.uml2.models.common.ReferenceType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MyClass implements JavaObject {
    private MyClass superClass = null;
    private final UmlClass umlClass;
    private final ArrayList<MyOperation> operations = new ArrayList<>();
    private final ArrayList<UmlAttribute> attributes = new ArrayList<>();
    private final ArrayList<MyInterface> interfaces = new ArrayList<>();
    private int sumOfSub = 0;
    private boolean dirty = false;
    private int couplingDegree = 0;
    
    public MyClass(UmlClass umlClass) {
        this.umlClass = umlClass;
    }
    
    public void addSub() {
        sumOfSub++;
    }
    
    public String getId() {
        return umlClass.getId();
    }
    
    public int getSumOfSub() {
        return sumOfSub;
    }
    
    public String getClassName() {
        return umlClass.getName();
    }
    
    @Override
    public void addAttribute(UmlAttribute umlAttribute) {
        attributes.add(umlAttribute);
    }
    
    @Override
    public void addOperation(MyOperation operation) {
        operations.add(operation);
    }
    
    public void setSuperClass(MyClass superClass) {
        this.superClass = superClass;
        superClass.addSub();
    }
    
    public int getDepth() {
        int cnt = 0;
        MyClass current = this;
        while (current.superClass != null) {
            cnt++;
            current = current.superClass;
        }
        return cnt;
    }
    
    public Map<Visibility, Integer> getOperationVisibility(String opName) {
        Map<Visibility, Integer> visibilityMap = new EnumMap<>(Visibility.class);
        for (MyOperation operation : operations) {
            if (operation.getName().equals(opName)) {
                visibilityMap.merge(operation.getVisibility(), 1, Integer::sum);
            }
        }
        return visibilityMap;
    }
    
    public ArrayList<MyInterface> getInterfaces() {
        return interfaces;
    }
    
    //avoid recursion
    public List<String> getInterfacesList() {
        HashSet<MyInterface> interfaces = new HashSet<>();
        // direct
        // father
        // extend
        MyClass current = this;
        //deal with itself and father
        while (current != null) {
            interfaces.addAll(current.getInterfaces());
            current = current.superClass;
        }
        Queue<MyInterface> interfaceQueue = new LinkedList<>(interfaces);
        Set<MyInterface> superInterfaces = new HashSet<>();
        // find the father of the interface
        while (!interfaceQueue.isEmpty()) {
            MyInterface interf = interfaceQueue.poll();
            if (!superInterfaces.contains(interf)) {
                superInterfaces.add(interf);
                interfaceQueue.addAll(interf.getSuperInterface());
            }
        }
        List<String> interfaceName = new ArrayList<>();
        for (MyInterface myInterface : superInterfaces) {
            interfaceName.add(myInterface.getName());
        }
        return interfaceName;
    }
    
    public int calculateCouplingDegree() {
        if (dirty) {
            return couplingDegree;
        }
        int degree = 0;
        //avoid recursion
        Set<ReferenceType> referenceTypes = new HashSet<>();
        MyClass current = this;
        while (current != null) {
            for (UmlAttribute umlAttribute : current.attributes) {
                if (umlAttribute.getType() instanceof NamedType) {
                    continue;
                }
                ReferenceType type = (ReferenceType) umlAttribute.getType();
                if (!umlClass.getId().equals(type.getReferenceId())
                        && !referenceTypes.contains(type)) {
                    degree++;
                    referenceTypes.add(type);
                }
            }
            current = current.superClass;
        }
        dirty = true;
        couplingDegree = degree;
        return degree;
    }
    
    public int getOperationsSize() {
        return operations.size();
    }
    
    public ArrayList<MyOperation> getOperations() {
        return operations;
    }
}
