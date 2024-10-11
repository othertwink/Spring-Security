package org.othertwink.employeeapp.exception.auth;

public class InvalidJwtException extends RuntimeException{
    public InvalidJwtException(String message) {
        super(message);
    }
}
