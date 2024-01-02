package com.server.todoapp.domain.dto;

import lombok.Data;

@Data
public class TodoResponse {

    private String title;

    private String description;

    private Integer todoId;
}
