package myjob;

import com.oocourse.uml1.models.common.Direction;
import com.oocourse.uml1.models.common.NamedType;
import com.oocourse.uml1.models.common.ReferenceType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyOperation {
    private final UmlOperation umlOperation;
    private final ArrayList<UmlParameter> params = new ArrayList<>();
    private final ArrayList<UmlParameter> returns = new ArrayList<>();
    private final Map<String, Integer> paramsList = new HashMap<>();
    private boolean errorType = false;
    
    public MyOperation(UmlOperation umlOperation) {
        this.umlOperation = umlOperation;
    }
    
    public String getId() {
        return umlOperation.getId();
    }
    
    public String getName() {
        return umlOperation.getName();
    }
    
    public Visibility getVisibility() {
        return umlOperation.getVisibility();
    }
    
    public boolean hasReturn() {
        return !returns.isEmpty();
    }
    
    public boolean hasParameters() {
        return !params.isEmpty();
    }
    
    public void addParameter(UmlParameter umlParameter) {
        // include return type and normal parameter
        boolean checker = true;
        if (umlParameter.getDirection() == Direction.RETURN) {
            if (umlParameter.getType() instanceof NamedType) {
                checker = checkType((NamedType) umlParameter.getType());
                if (((NamedType) umlParameter.getType()).getName().equals("void")) {
                    checker = true;
                }
            }
            if (checker) {
                returns.add(umlParameter);
            } else {
                errorType = true;
            }
        } else if (umlParameter.getDirection() == Direction.IN) {
            if (umlParameter.getType() instanceof NamedType) {
                checker = checkType((NamedType) umlParameter.getType());
            }
            if (checker) {
                params.add(umlParameter);
                if (umlParameter.getType() instanceof NamedType) {
                    NamedType type = (NamedType) umlParameter.getType();
                    paramsList.merge(type.getName(), 1, Integer::sum);
                } else if (umlParameter.getType() instanceof ReferenceType) {
                    ReferenceType type = (ReferenceType) umlParameter.getType();
                    paramsList.merge(type.getReferenceId(), 1, Integer::sum);
                }
            } else {
                errorType = true;
            }
        } else {
            errorType = true;
        }
    }
    
    public boolean isErrorType() {
        return errorType;
    }
    
    public boolean checkType(NamedType type) {
        switch (type.getName()) {
            case "int":
            case "double":
            case "boolean":
            case "short":
            case "float":
            case "long":
            case "char":
            case "byte":
            case "String":
                return true;
            default:
                return false;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        // System.out.println("IN");
        if (!(obj instanceof MyOperation)) {
            return false;
        }
        MyOperation myOperation = (MyOperation) obj;
        if (myOperation.isErrorType()) {
            return false;
        }
        if (getParamsSize() != myOperation.getParamsSize()) {
            return false;
        }
        boolean checker = true;
        for (String parameter : this.paramsList.keySet()) {
            if (!myOperation.paramsList.containsKey(parameter)
                    || !myOperation.paramsList.get(parameter).equals(paramsList.get(parameter))) {
                checker = false;
                break;
            }
        }
        // System.out.println("checker = " + checker);
        return checker && myOperation.getName().equals(getName());
    }
    
    public ArrayList<UmlParameter> getParams() {
        return params;
    }
    
    public int getParamsSize() {
        return params.size();
    }
    
    public int calculateCouplingDegree(String id) {
        int degree = 0;
        Set<ReferenceType> referenceTypes = new HashSet<>();
        List<UmlParameter> allParams = new ArrayList<>(params);
        allParams.addAll(returns);
        for (UmlParameter parameter : allParams) {
            if (parameter.getType() instanceof ReferenceType) {
                ReferenceType type = (ReferenceType) parameter.getType();
                if (!type.getReferenceId().equals(id)
                        && !referenceTypes.contains(type)) {
                    degree++;
                    referenceTypes.add(type);
                }
            }
        }
        return degree;
    }
}
