package com.pm.patientservice.exception;

public class UserIdDoesNotExistsException extends RuntimeException {
    public UserIdDoesNotExistsException(String message) {
        super(message);
    }
}
