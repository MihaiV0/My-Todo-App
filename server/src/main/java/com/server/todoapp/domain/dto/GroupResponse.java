package com.server.todoapp.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupResponse {

    private Integer groupId;

    private String groupName;

    private List<String> members;

    private List<GroupMessage> messages;
}
