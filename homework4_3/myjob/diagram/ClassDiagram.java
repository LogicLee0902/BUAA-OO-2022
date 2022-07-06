package myjob.diagram;

import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;
import myjob.JavaObject;
import myjob.MyClass;
import myjob.MyInterface;
import myjob.MyOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassDiagram {
    private final Map<String, Object> id2elements;
    private final Map<String, List<MyClass>> name2Classes = new HashMap<>();
    private final List<MyInterface> interfaces = new ArrayList<>();
    
    public ClassDiagram(Map<String, Object> id2Elements) {
        this.id2elements = id2Elements;
    }
    
    public void parseAttribute(UmlAttribute umlAttribute) {
        id2elements.put(umlAttribute.getId(), umlAttribute);
        Object parent = id2elements.get(umlAttribute.getParentId());
        if (parent instanceof JavaObject) {
            ((JavaObject) parent).addAttribute(umlAttribute);
        }
    }
    
    public void parseClass(UmlClass umlClass) {
        MyClass myClass = new MyClass(umlClass);
        id2elements.put(umlClass.getId(), myClass);
        if (name2Classes.containsKey(umlClass.getName())) {
            name2Classes.get(umlClass.getName()).add(myClass);
        } else {
            List<MyClass> temp = new ArrayList<>();
            temp.add(myClass);
            name2Classes.put(umlClass.getName(), temp);
        }
    }
    
    public void parseInterface(UmlInterface umlInterface) {
        MyInterface myInterface = new MyInterface(umlInterface);
        interfaces.add(myInterface);
        id2elements.put(umlInterface.getId(), myInterface);
    }
    
    public void parseGeneralization(UmlGeneralization generalization) {
        Object source = id2elements.get(generalization.getSource());
        Object target = id2elements.get(generalization.getTarget());
        if (source instanceof MyClass && target instanceof MyClass) {
            ((MyClass) source).setSuperClass((MyClass) target);
        } else if (source instanceof MyInterface && target instanceof MyInterface) {
            ((MyInterface) source).setSuperInterface((MyInterface) target);
        }
    }
    
    public void parseInterfaceRealization(UmlInterfaceRealization uml) {
        Object source = id2elements.get(uml.getSource());
        Object target = id2elements.get(uml.getTarget());
        if (source instanceof MyClass && target instanceof MyInterface) {
            ((MyClass) source).getInterfaces().add((MyInterface) target);
        }
    }
    
    public void parseOperation(UmlOperation umlOperation) {
        MyOperation myOperation = new MyOperation(umlOperation);
        JavaObject parent = (JavaObject) id2elements.get(umlOperation.getParentId());
        parent.addOperation(myOperation);
        id2elements.put(umlOperation.getId(), myOperation);
    }
    
    public void parseParameter(UmlParameter umlParameter) {
        MyOperation myOperation = (MyOperation) id2elements.get(umlParameter.getParentId());
        myOperation.addParameter(umlParameter);
    }
    
    public void parseAssociation(UmlAssociation umlAssociation) {
        UmlAssociationEnd end1 = (UmlAssociationEnd) id2elements.get(umlAssociation.getEnd1());
        UmlAssociationEnd end2 = (UmlAssociationEnd) id2elements.get(umlAssociation.getEnd2());
        JavaObject class1 = (JavaObject) id2elements.get(end1.getReference());
        JavaObject class2 = (JavaObject) id2elements.get(end2.getReference());
        class1.addEnds(end2);
        class2.addEnds(end1);
    }
    
    public void parseAssociationEnd(UmlAssociationEnd umlAssociationEnd) {
        id2elements.put(umlAssociationEnd.getId(), umlAssociationEnd);
    }
    
    public int getClassSubClassCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).getSumOfSub();
    }
    
    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).getOperationsSize();
    }
    
    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).getOperationVisibility(methodName);
    }
    
    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        List<Integer> result = new ArrayList<>();
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        MyClass myClass = name2Classes.get(className).get(0);
        List<MyOperation> operations = new ArrayList<>();
        for (MyOperation myOperation : myClass.getOperations()) {
            if (!myOperation.getName().equals(methodName)) {
                continue;
            }
            if (myOperation.isErrorType()) {
                throw new MethodWrongTypeException(myClass.getClassName(), methodName);
            }
            if (operations.contains(myOperation)) {
                throw new MethodDuplicatedException(className, methodName);
            }
            operations.add(myOperation);
            result.add(myOperation.calculateCouplingDegree(myClass.getId()));
        }
        return result;
    }
    
    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).calculateCouplingDegree();
    }
    
    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).getInterfacesList();
    }
    
    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).getDepth();
    }
    
    public void checkForUml005() throws UmlRule005Exception {
        for (MyInterface myInterface : interfaces) {
            myInterface.checkVisibility();
        }
    }
    
    public void checkForUml001() throws UmlRule001Exception {
        for (String name : name2Classes.keySet()) {
            for (MyClass myClass : name2Classes.get(name)) {
                myClass.checkName();
            }
        }
        for (MyInterface myInterface : interfaces) {
            myInterface.checkName();
        }
    }
    
    public void checkForUml002() throws UmlRule002Exception {
        Set<AttributeClassInformation> errors = new HashSet<>();
        for (String name : name2Classes.keySet()) {
            for (MyClass myClass : name2Classes.get(name)) {
                errors.addAll(myClass.checkDuplicatedName());
            }
        }
        if (!errors.isEmpty()) {
            throw new UmlRule002Exception(errors);
        }
    }
    
    public void checkForUml003() throws UmlRule003Exception {
        Set<UmlClassOrInterface> circular = new HashSet<>();
        for (String name : name2Classes.keySet()) {
            for (MyClass myClass : name2Classes.get(name)) {
                if (!myClass.checkInheritance()) {
                    circular.add(myClass.getUmlClass());
                }
            }
        }
        for (MyInterface myInterface : interfaces) {
            if (!myInterface.checkInheritance()) {
                circular.add(myInterface.getUmlInterface());
            }
        }
        
        if (!circular.isEmpty()) {
            throw new UmlRule003Exception(circular);
        }
    }
    
    public void checkForUml004() throws UmlRule004Exception {
        // ignore class
        Set<UmlClassOrInterface> duplicated = new HashSet<>();
        for (MyInterface myInterface : interfaces) {
            if (!myInterface.checkGeneralization()) {
                duplicated.add(myInterface.getUmlInterface());
            }
        }
        if (!duplicated.isEmpty()) {
            throw new UmlRule004Exception(duplicated);
        }
    }
}