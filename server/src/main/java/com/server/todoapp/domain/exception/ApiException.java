package com.server.todoapp.domain.exception;

public class ApiException extends Exception {
    public ApiException(String message) {
        super(message);
    }
}
