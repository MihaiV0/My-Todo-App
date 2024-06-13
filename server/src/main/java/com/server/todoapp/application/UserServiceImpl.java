package com.server.todoapp.application;

import com.server.todoapp.domain.dto.UserLoginDto;
import com.server.todoapp.domain.dto.UserPatchRequest;
import com.server.todoapp.domain.dto.UserRegisterDto;
import com.server.todoapp.domain.dto.UserResponseDto;
import com.server.todoapp.domain.entity.User;
import com.server.todoapp.domain.exception.ApiException;
import com.server.todoapp.domain.exception.EmailAlreadyExistsException;
import com.server.todoapp.domain.exception.InvalidLoginCredentialsException;
import com.server.todoapp.domain.exception.UsernameAlreadyExistsException;
import com.server.todoapp.domain.repository.UserRepository;
import com.server.todoapp.utils.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserResponseDto registerUser(UserRegisterDto userRegisterDto)
            throws UsernameAlreadyExistsException, EmailAlreadyExistsException, ApiException {
        User user = new User(userRegisterDto.getUsername(), userRegisterDto.getEmail(), userRegisterDto.getPassword());

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username " + user.getUsername() + " is already used!");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email " + user.getEmail() + " is already used!");
        }

        UserResponseDto response = modelMapper.map(userRepository.save(user), UserResponseDto.class);
        try {
            EmailService.sendEmail(response.getEmail());
        } catch (MessagingException e) {
            throw new ApiException("Email couldn't be sent");
        } catch (RuntimeException e) {
            throw new ApiException(e.getMessage());
        }
        return response;
    }

    @Override
    public UserResponseDto loginUser(UserLoginDto userLoginDto)
            throws InvalidLoginCredentialsException {
        Optional<User> user = userRepository.findByUsernameOrEmail(userLoginDto.getUsernameOrEmail());

        if (user.isPresent()) {
            if (user.get().getPassword().equals(userLoginDto.getPassword())) {
                return modelMapper.map(user.get(), UserResponseDto.class);
            } else {
                throw new InvalidLoginCredentialsException("Incorrect username or password");
            }
        } else {
            throw new InvalidLoginCredentialsException("Incorrect username or password");
        }
    }

    @Override
    public UserResponseDto updateUser(UserPatchRequest request, String username)
            throws ApiException {
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new ApiException("User with username " + username + " doesn't exist"));

        AtomicBoolean isUserFound = new AtomicBoolean(false);
        userRepository.findByUsername(request.getUsername()).ifPresent(userFound -> {
            isUserFound.set(!(request.getUsername().equals(username)));
        });
        if (isUserFound.get()) {
            throw new ApiException("Username " + request.getUsername() + " is already used");
        }

        AtomicBoolean isEmailFound = new AtomicBoolean(false);
        userRepository.findByEmail(request.getEmail()).ifPresent(userFound -> {
            isEmailFound.set(!(request.getEmail().equals(user.getEmail())));
        });
        if (isEmailFound.get()) {
            throw new ApiException("Email " + request.getEmail() + " is already used");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        return modelMapper.map(userRepository.save(user), UserResponseDto.class);
    }
}
