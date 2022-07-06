package myjob;

import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class MyInterface implements JavaObject {
    private final List<MyInterface> superInterface = new ArrayList<>();
    private final ArrayList<UmlAttribute> attributes = new ArrayList<>();
    private final ArrayList<MyOperation> operations = new ArrayList<>();
    private final ArrayList<UmlAssociationEnd> ends = new ArrayList<>();
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
    public void addEnds(UmlAssociationEnd end) {
        ends.add(end);
    }
    
    public void checkVisibility() throws UmlRule005Exception {
        for (UmlAttribute umlAttribute : attributes) {
            if (umlAttribute.getVisibility() != Visibility.PUBLIC) {
                throw new UmlRule005Exception();
            }
        }
    }
    
    public void checkName() throws UmlRule001Exception {
        if (getName() == null || getName().trim().isEmpty()) {
            throw new UmlRule001Exception();
        }
        for (UmlAttribute attribute : attributes) {
            if (attribute.getName() == null || attribute.getName().trim().isEmpty()) {
                throw new UmlRule001Exception();
            }
        }
        for (MyOperation operation : operations) {
            operation.checkName();
        }
    }
    
    public boolean checkGeneralization() {
        Set<MyInterface> generalizedInterface = new HashSet<>();
        Queue<MyInterface> queue = new LinkedList<>(superInterface);
        while (!queue.isEmpty()) {
            MyInterface current = queue.poll();
            if (generalizedInterface.contains(current)) {
                return false;
            } else {
                generalizedInterface.add(current);
                queue.addAll(current.superInterface);
            }
        }
        return true;
    }
    
    public UmlInterface getUmlInterface() {
        return umlInterface;
    }
    
    public boolean checkInheritance() {
        // avoid recurrence
        Stack<MyInterface> stack = new Stack<>();
        // acquire the super interface by order
        Stack<Integer> index = new Stack<>();
        stack.add(this);
        index.add(0);
        Set<MyInterface> visited = new HashSet<>();
        visited.add(this);
        while (!stack.isEmpty()) {
            int i = index.pop();
            MyInterface myInterface = stack.pop();
            // if there are some super interfaces that hasn't been visited
            if (i < myInterface.superInterface.size()) {
                MyInterface next = myInterface.superInterface.get(i);
                if (next == this) {
                    return false;
                }
                index.push(i + 1);
                stack.push(myInterface);
                if (!visited.contains(next)) {
                    visited.add(next);
                    stack.add(next);
                    index.add(0);
                }
            }
            // each stack and index is synchronised due to being operated same time
        }
        return true;
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
