package com.server.todoapp.domain.dto;

import com.server.todoapp.domain.data.types.Priority;
import com.server.todoapp.domain.data.types.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TodoResponse {

    private String title;

    private String description;

    private Integer todoId;

    private LocalDate dueDate;

    private Status status;

    private Priority priority;
}
