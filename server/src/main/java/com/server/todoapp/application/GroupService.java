package com.server.todoapp.application;

import com.server.todoapp.domain.dto.GroupPostRequest;
import com.server.todoapp.domain.dto.GroupResponse;
import com.server.todoapp.domain.exception.ApiException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupService {
    List<GroupResponse> getAllGroups();

    GroupResponse insertGroup(String username, GroupPostRequest request) throws ApiException;
}
