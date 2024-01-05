package com.server.todoapp.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TodoResponse {

    private String title;

    private String description;

    private Integer todoId;

    private LocalDate dueDate;
}
