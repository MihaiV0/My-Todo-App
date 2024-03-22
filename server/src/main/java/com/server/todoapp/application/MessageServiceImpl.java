package com.server.todoapp.application;

import com.server.todoapp.application.api.helper.ApiHelper;
import com.server.todoapp.domain.dto.GroupMessage;
import com.server.todoapp.domain.entity.Message;
import com.server.todoapp.domain.repository.GroupRepository;
import com.server.todoapp.domain.repository.MessageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    private final GroupRepository groupRepository;

    private final ModelMapper modelMapper;

    public MessageServiceImpl(MessageRepository messageRepository, GroupRepository groupRepository, ModelMapper modelMapper) {
        this.messageRepository = messageRepository;
        this.groupRepository = groupRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<GroupMessage> loadAllMessagesForGroup(String groupName) {
        int groupId = groupRepository.findByGroupName(groupName).get().getGroupId();
        List<GroupMessage> messages = ApiHelper.convertListToDestinationType(messageRepository.findByGroupId(groupId),
                modelMapper, Message.class, GroupMessage.class);
        Collections.sort(messages);
        return messages;
    }
}
