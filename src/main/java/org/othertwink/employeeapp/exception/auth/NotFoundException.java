package org.othertwink.employeeapp.exception.auth;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
