package com.server.todoapp.application;

import com.server.todoapp.domain.dto.GroupMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {
    List<GroupMessage> loadAllMessagesForGroup(String groupName);
}
