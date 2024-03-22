package com.server.todoapp.domain.dto;

import com.server.todoapp.domain.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class GroupResponse {

    private Integer groupId;

    private String groupName;

    private List<String> members;
}
