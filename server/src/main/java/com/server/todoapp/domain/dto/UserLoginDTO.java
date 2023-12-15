package com.server.todoapp.domain.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String usernameOrEmail;
    private String password;
}
