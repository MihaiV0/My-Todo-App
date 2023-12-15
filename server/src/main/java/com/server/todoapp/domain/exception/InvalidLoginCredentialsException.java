package com.server.todoapp.domain.exception;

public class InvalidLoginCredentialsException extends Exception{
    public InvalidLoginCredentialsException(String message) {
        super(message);
    }
}
