package com.server.todoapp.application;

import com.server.todoapp.domain.dto.UserLoginDto;
import com.server.todoapp.domain.dto.UserPatchRequest;
import com.server.todoapp.domain.dto.UserRegisterDto;
import com.server.todoapp.domain.dto.UserResponseDto;
import com.server.todoapp.domain.exception.ApiException;
import com.server.todoapp.domain.exception.EmailAlreadyExistsException;
import com.server.todoapp.domain.exception.InvalidLoginCredentialsException;
import com.server.todoapp.domain.exception.UsernameAlreadyExistsException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserResponseDto registerUser(UserRegisterDto userRegisterDTO) throws UsernameAlreadyExistsException, EmailAlreadyExistsException;

    UserResponseDto loginUser(UserLoginDto userLoginDTO) throws InvalidLoginCredentialsException;

    UserResponseDto updateUser(UserPatchRequest request, String username) throws ApiException;
}
