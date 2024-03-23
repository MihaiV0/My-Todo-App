package com.server.todoapp.application;

import com.server.todoapp.application.api.helper.ApiHelper;
import com.server.todoapp.domain.dto.GroupMessage;
import com.server.todoapp.domain.dto.MessagePostRequest;
import com.server.todoapp.domain.entity.Group;
import com.server.todoapp.domain.entity.Message;
import com.server.todoapp.domain.entity.User;
import com.server.todoapp.domain.exception.ApiException;
import com.server.todoapp.domain.repository.GroupRepository;
import com.server.todoapp.domain.repository.MessageRepository;
import com.server.todoapp.domain.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public MessageServiceImpl(MessageRepository messageRepository, GroupRepository groupRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.messageRepository = messageRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
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

    @Override
    public GroupMessage insertMessage(MessagePostRequest request)
            throws ApiException {
        String username = ApiHelper.replaceSpacesWithUnderscores(request.getUsername());
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User " + username + " does not exist"));
        Group group = groupRepository.findByGroupName(request.getGroupName())
                .orElseThrow(() -> new ApiException("Group " + request.getGroupName() + " does not exist"));
        if (!group.getMembers().contains(user)) {
            throw new ApiException("User " + username + " is not part of group " + group.getGroupName());
        }

        Message message = new Message();
        message.setMessage(request.getMessage());
        message.setUsername(ApiHelper.replaceUnderscoresWithSpaces(request.getUsername()));
        message.setDateTime(LocalDateTime.now());
        message.setGroup(group);

        if (group.getMessages() == null) {
            group.setMessages(new ArrayList<>());
        }
        group.getMessages().add(message);
        groupRepository.save(group);

        return ApiHelper.convertMessageToGroupMessage(messageRepository.save(message), modelMapper);
    }
}
