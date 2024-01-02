package com.server.todoapp.domain.dto;

import lombok.Data;

@Data
public class TodoPostRequest {

    private String title;

    private String description;

    private String username;
}
