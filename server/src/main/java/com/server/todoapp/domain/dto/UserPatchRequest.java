package com.server.todoapp.domain.dto;

import lombok.Data;

@Data
public class UserPatchRequest {
    private String username;
    private String email;
}
