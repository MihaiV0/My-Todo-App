package com.server.todoapp.application.api.helper;

import com.server.todoapp.domain.dto.GroupMessage;
import com.server.todoapp.domain.dto.GroupResponse;
import com.server.todoapp.domain.entity.Group;
import com.server.todoapp.domain.entity.Message;
import com.server.todoapp.utils.DateUtils;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiHelper {
    public static <T, V> List<V> convertListToDestinationType(List<T> allItems,
                                                              final ModelMapper modelMapper,
                                                              Class<T> initialType,
                                                              Class<V> destinationType) {
        List<V> allItemsConverted = new ArrayList<>();
        for (int i = 0; i < allItems.size(); i++) {
            if (destinationType == GroupMessage.class) {
                GroupMessage groupMessage = modelMapper.map(allItems.get(i), GroupMessage.class);
                groupMessage.setDateTime(DateUtils.getDateAndTime(((Message) allItems.get(i)).getDateTime()));
                allItemsConverted.add((V) groupMessage);
            } else if (destinationType == GroupResponse.class) {
                allItemsConverted.add((V) convertGroupToGroupResponse((Group) allItems.get(i)));
            } else {
                allItemsConverted.add(modelMapper.map(allItems.get(i), destinationType));
            }
        }

        return allItemsConverted;
    }

    public static String replaceSpacesWithUnderscores(String initial) {
        StringBuilder sb = new StringBuilder();
        String[] parts = initial.split(" ");
        sb.append(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            sb.append("_");
            sb.append(parts[i]);
        }
        return sb.toString();
    }

    public static String replaceUnderscoresWithSpaces(String initial) {
        StringBuilder sb = new StringBuilder();
        String[] parts = initial.split("_");
        sb.append(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            sb.append(" ");
            sb.append(parts[i]);
        }
        return sb.toString();
    }

    public static GroupResponse convertGroupToGroupResponse(Group group) {
        GroupResponse response = new GroupResponse();
        response.setGroupId(group.getGroupId());
        response.setGroupName(group.getGroupName());
        response.setMembers(new ArrayList<>());
        response.setMessages(new ArrayList<>());
        Collections.sort(group.getMessages());
        for (int i = 0; i < group.getMembers().size(); i++) {
            response.getMembers().add(group.getMembers().get(i).getUsername());
        }
        for (int i = 0; i < group.getMessages().size(); i++) {
            GroupMessage message = new GroupMessage();
            message.setMessage(group.getMessages().get(i).getMessage());
            message.setUsername(group.getMessages().get(i).getUsername());
            message.setDateTime(DateUtils.getDateAndTime(group.getMessages().get(i).getDateTime()));
            response.getMessages().add(message);
        }
        return response;
    }
}
