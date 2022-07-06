package com.oocourse.uml3.args.validations;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import com.oocourse.uml3.models.top.TopModelType;

public class TopModelTypeValidation implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (!TopModelType.containsOriginalString(value)) {
            throw new ParameterException(String.format("Type \"%s\" not supported yet.", value));
        }
    }
}
