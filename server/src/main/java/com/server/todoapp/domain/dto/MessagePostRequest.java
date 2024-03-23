package com.server.todoapp.domain.dto;

import lombok.Data;

@Data
public class MessagePostRequest {
    private String message;
    private String username;
    private String groupName;
}
