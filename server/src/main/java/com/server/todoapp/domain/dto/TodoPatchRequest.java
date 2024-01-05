package com.server.todoapp.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TodoPatchRequest {

    private String title;

    private String description;

    private String dueDate;
}
