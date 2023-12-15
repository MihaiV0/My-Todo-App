package com.server.todoapp.application;

import com.server.todoapp.domain.dto.UserLoginDTO;
import com.server.todoapp.domain.dto.UserRegisterDTO;
import com.server.todoapp.domain.dto.UserResponseDTO;
import com.server.todoapp.domain.exception.EmailAlreadyExistsException;
import com.server.todoapp.domain.exception.InvalidLoginCredentialsException;
import com.server.todoapp.domain.exception.UsernameAlreadyExistsException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public UserResponseDTO registerUser(UserRegisterDTO userRegisterDTO) throws UsernameAlreadyExistsException, EmailAlreadyExistsException;

    public UserResponseDTO loginUser(UserLoginDTO userLoginDTO) throws InvalidLoginCredentialsException;
}
