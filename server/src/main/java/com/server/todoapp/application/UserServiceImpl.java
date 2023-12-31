package com.server.todoapp.application;

import com.server.todoapp.domain.dto.UserLoginDto;
import com.server.todoapp.domain.dto.UserRegisterDto;
import com.server.todoapp.domain.dto.UserResponseDto;
import com.server.todoapp.domain.entity.User;
import com.server.todoapp.domain.exception.EmailAlreadyExistsException;
import com.server.todoapp.domain.exception.InvalidLoginCredentialsException;
import com.server.todoapp.domain.exception.UsernameAlreadyExistsException;
import com.server.todoapp.domain.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserResponseDto registerUser(UserRegisterDto userRegisterDto)
            throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        User user = new User(userRegisterDto.getUsername(), userRegisterDto.getEmail(), userRegisterDto.getPassword());

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username " + user.getUsername() + " is already used!");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email " + user.getEmail() + " is already used!");
        }

        return modelMapper.map(userRepository.save(user), UserResponseDto.class);
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
}
