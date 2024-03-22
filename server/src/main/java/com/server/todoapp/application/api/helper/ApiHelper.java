package com.server.todoapp.application.api.helper;

import com.server.todoapp.domain.dto.GroupResponse;
import com.server.todoapp.domain.entity.Group;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class ApiHelper {
    public static <T, V> List<V> convertListToDestinationType(List<T> allItems,
                                                              final ModelMapper modelMapper,
                                                              Class<T> initialType,
                                                              Class<V> destinationType) {
        List<V> allItemsConverted = new ArrayList<>();
        for (int i = 0; i < allItems.size(); i++) {
            if (destinationType == GroupResponse.class) {
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

    public static GroupResponse convertGroupToGroupResponse(Group group) {
        GroupResponse response = new GroupResponse();
        response.setGroupId(group.getGroupId());
        response.setGroupName(group.getGroupName());
        response.setMembers(new ArrayList<>());
        for (int j = 0; j < group.getMembers().size(); j++) {
            response.getMembers().add(group.getMembers().get(j).getUsername());
        }
        return response;
    }
}
