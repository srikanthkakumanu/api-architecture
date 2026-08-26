package com.apidemo.rest.validation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

public class ToDoValidationError {

    @JsonInclude(JsonInclude.Include.NON_EMPTY) // It says that even even if the errors field is empty, it must be included
    private List<String> errors = new ArrayList<>();
    private final String message;

    public ToDoValidationError(String message) { this.message = message; }

    public void addValidationError(String message) { errors.add(message); }

    public List<String> getErrors() { return errors; }

    public String getErrorMessage() { return message; }
}
