package com.server.todoapp.domain.dto;

import lombok.Data;

@Data
public class TodoPatchRequest {

    private String title;

    private String description;
}
