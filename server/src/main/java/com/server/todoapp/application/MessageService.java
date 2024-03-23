package com.server.todoapp.application;

import com.server.todoapp.domain.dto.GroupMessage;
import com.server.todoapp.domain.dto.MessagePostRequest;
import com.server.todoapp.domain.exception.ApiException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {
    List<GroupMessage> loadAllMessagesForGroup(String groupName);
    GroupMessage insertMessage(MessagePostRequest request) throws ApiException;
}
