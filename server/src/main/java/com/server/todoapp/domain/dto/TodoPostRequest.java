package com.server.todoapp.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TodoPostRequest {

    private String title;

    private String description;

    private String username;

    private String dueDate;
}
