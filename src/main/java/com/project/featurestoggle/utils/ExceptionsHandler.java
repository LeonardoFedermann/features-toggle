package com.project.featurestoggle.utils;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public NotFoundException handleNotFoundError() {
        throw new NotFoundException("Not found");
//        return ResponseEntity.notFound().build();
    }
}
