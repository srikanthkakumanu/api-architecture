package com.apidemo.rest.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class ToDoValidationErrorBuilder {
    
    public static ToDoValidationError fromBindingErrors(Errors errors) {
        
        String err = "Validation failed. " + errors.getErrorCount() + " error(s)";
        ToDoValidationError error = new ToDoValidationError(err);

        for(ObjectError objError : errors.getAllErrors())
            error.addValidationError(objError.getDefaultMessage());
        
        return error;        
    }
}
