package com.project.featurestoggle.dtos;

import org.springframework.validation.FieldError;

public record ValidationErrorData(String field, String message) {
    public ValidationErrorData(FieldError fieldError) {
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
