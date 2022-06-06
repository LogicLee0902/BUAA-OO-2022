package myjob;

import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml1.interact.format.UserApi;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyImplementation implements UserApi {
    private final HashMap<String, Object> id2elements = new HashMap<>();
    private final HashMap<String, List<MyClass>> name2Classes = new HashMap<>();
    private int count = 0;
    
    public MyImplementation(UmlElement... elements) {
        // CLass, Interface
        // AssociationEnd, Attribute, Generalization,InterfaceRealization, Operation
        // association, params
        for (UmlElement element : elements) {
            if (element instanceof UmlClass) {
                count++;
                parseClass((UmlClass) element);
            } else if (element instanceof UmlInterface) {
                MyInterface myInterface = new MyInterface((UmlInterface) element);
                id2elements.put(element.getId(), myInterface);
            }
        }
        for (UmlElement element : elements) {
            if (element instanceof UmlAttribute) {
                JavaObject parent = (JavaObject) id2elements.get(element.getParentId());
                parent.addAttribute((UmlAttribute) element);
            } else if (element instanceof UmlOperation) {
                parseOperation((UmlOperation) element);
            } else if (element instanceof UmlGeneralization) {
                parseGeneralization((UmlGeneralization) element);
            } else if (element instanceof UmlInterfaceRealization) {
                parseInterfaceRealization((UmlInterfaceRealization) element);
            }
        }
        
        for (UmlElement element : elements) {
            if (element instanceof UmlParameter) {
                MyOperation myOperation = (MyOperation) id2elements.get(element.getParentId());
                myOperation.addParameter((UmlParameter) element);
            }
        }
    }
    
    private void parseClass(UmlClass umlClass) {
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
    
    private void parseGeneralization(UmlGeneralization generalization) {
        Object source = id2elements.get(generalization.getSource());
        Object target = id2elements.get(generalization.getTarget());
        if (source instanceof MyClass && target instanceof MyClass) {
            ((MyClass) source).setSuperClass((MyClass) target);
        } else if (source instanceof MyInterface && target instanceof MyInterface) {
            ((MyInterface) source).setSuperInterface((MyInterface) target);
        }
    }
    
    private void parseInterfaceRealization(UmlInterfaceRealization uml) {
        Object source = id2elements.get(uml.getSource());
        Object target = id2elements.get(uml.getTarget());
        if (source instanceof MyClass && target instanceof MyInterface) {
            ((MyClass) source).getInterfaces().add((MyInterface) target);
        }
    }
    
    private void parseOperation(UmlOperation umlOperation) {
        MyOperation myOperation = new MyOperation(umlOperation);
        JavaObject parent = (JavaObject) id2elements.get(umlOperation.getParentId());
        parent.addOperation(myOperation);
        id2elements.put(umlOperation.getId(), myOperation);
    }
    
    @Override
    public int getClassCount() {
        return count;
    }
    
    @Override
    public int getClassSubClassCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).getSumOfSub();
    }
    
    @Override
    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).getOperationsSize();
    }
    
    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).getOperationVisibility(methodName);
    }
    
    @Override
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
    
    @Override
    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).calculateCouplingDegree();
    }
    
    @Override
    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).getInterfacesList();
    }
    
    @Override
    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2Classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).get(0).getDepth();
    }
    
}
