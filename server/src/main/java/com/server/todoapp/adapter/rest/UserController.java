package com.server.todoapp.adapter.rest;

import com.server.todoapp.application.UserService;
import com.server.todoapp.domain.dto.UserLoginDTO;
import com.server.todoapp.domain.dto.UserRegisterDTO;
import com.server.todoapp.domain.dto.UserResponseDTO;
import com.server.todoapp.domain.exception.EmailAlreadyExistsException;
import com.server.todoapp.domain.exception.InvalidLoginCredentialsException;
import com.server.todoapp.domain.exception.UsernameAlreadyExistsException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRegisterDTO userRegisterDTO)
            throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        return new ResponseEntity<>(userService.registerUser(userRegisterDTO), HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@RequestBody UserLoginDTO userLoginDTO)
            throws InvalidLoginCredentialsException {
        return new ResponseEntity<>(userService.loginUser(userLoginDTO), HttpStatus.OK);
    }
}
