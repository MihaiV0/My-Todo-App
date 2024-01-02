package com.server.todoapp.utils;

import com.server.todoapp.domain.exception.EmailAlreadyExistsException;
import com.server.todoapp.domain.exception.InvalidLoginCredentialsException;
import com.server.todoapp.domain.exception.UserNotFoundException;
import com.server.todoapp.domain.exception.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UsernameAlreadyExistsException.class)
    public ResponseEntity<String> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidLoginCredentialsException.class)
    public ResponseEntity<String> handleInvalidLoginCredentialsException(InvalidLoginCredentialsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
