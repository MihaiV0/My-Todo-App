package com.server.todoapp.domain.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String message) {
        super(message);
    }
}
