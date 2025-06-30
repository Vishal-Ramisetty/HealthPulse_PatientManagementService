package com.pm.patientservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        HashMap<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error-> errors.put(error.getDefaultMessage(), error.getDefaultMessage())));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex){
        HashMap<String, String> errors = new HashMap<>();
        log.warn("Email id already exists !!! {}",ex.getMessage());
        errors.put("Error Message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UserIdDoesNotExistsException.class)
    public ResponseEntity<?> handleUserIdDoesNotExistsException(UserIdDoesNotExistsException ex){
        HashMap<String, String> errors = new HashMap<>();
        log.warn("Patient with id does not exists!!! {}",ex.getMessage());
        errors.put("Error Message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }
}
