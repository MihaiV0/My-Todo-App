package com.server.todoapp.adapter.rest;

import com.server.todoapp.application.UserService;
import com.server.todoapp.domain.dto.UserLoginDto;
import com.server.todoapp.domain.dto.UserRegisterDto;
import com.server.todoapp.domain.dto.UserResponseDto;
import com.server.todoapp.domain.exception.EmailAlreadyExistsException;
import com.server.todoapp.domain.exception.InvalidLoginCredentialsException;
import com.server.todoapp.domain.exception.UsernameAlreadyExistsException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Transactional
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRegisterDto userRegisterDto)
            throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        return new ResponseEntity<>(userService.registerUser(userRegisterDto), HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> loginUser(@RequestBody UserLoginDto userLoginDto)
            throws InvalidLoginCredentialsException {
        return new ResponseEntity<>(userService.loginUser(userLoginDto), HttpStatus.OK);
    }
}
