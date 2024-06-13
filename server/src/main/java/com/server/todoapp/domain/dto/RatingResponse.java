package com.server.todoapp.domain.dto;

import lombok.Data;

@Data
public class RatingResponse {
    private Double value;
    private Integer userId;
}
