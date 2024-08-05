package com.project.featurestoggle.infra.advices;

import com.project.featurestoggle.dtos.ValidationErrorData;
import com.project.featurestoggle.exceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundError() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationError(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getFieldErrors();
        return ResponseEntity.badRequest().body(fieldErrors.stream().map(ValidationErrorData::new).toList());
    }
}
