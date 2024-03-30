package com.server.todoapp.application;

import com.server.todoapp.application.api.helper.ApiHelper;
import com.server.todoapp.domain.dto.GroupPostRequest;
import com.server.todoapp.domain.dto.GroupResponse;
import com.server.todoapp.domain.entity.Group;
import com.server.todoapp.domain.entity.User;
import com.server.todoapp.domain.exception.ApiException;
import com.server.todoapp.domain.repository.GroupRepository;
import com.server.todoapp.domain.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class GroupServiceImpl implements GroupService {

    private final ModelMapper modelMapper;

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    public GroupServiceImpl(ModelMapper modelMapper, GroupRepository groupRepository, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<GroupResponse> getAllGroups() {
        return ApiHelper.convertListToDestinationType(groupRepository.getAllGroups(), modelMapper,
                Group.class, GroupResponse.class);
    }

    @Override
    public GroupResponse insertGroup(String username, GroupPostRequest request)
            throws ApiException {
        if (groupRepository.findByGroupName(request.getGroupName()).isPresent()) {
            throw new ApiException("Group with name " + request.getGroupName() + " already exists!");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User " + username + " doesn't exist!"));

        Group group = new Group();
        group.setGroupName(request.getGroupName());
        group.setMessages(new ArrayList<>());

        if (group.getMembers() == null) {
            group.setMembers(new ArrayList<>());
        }
        group.getMembers().add(user);

        if (user.getGroups() == null) {
            user.setGroups(new ArrayList<>());
        }
        user.getGroups().add(group);
        userRepository.save(user);

        return ApiHelper.convertGroupToGroupResponse(groupRepository.save(group));
    }

    @Override
    public GroupResponse addUserToGroup(String groupName, String username)
            throws ApiException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User " + username + " not found"));
        Group group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new ApiException("Group " + groupName + " not found"));
        for (int i = 0; i < group.getMembers().size(); i++) {
            if (group.getMembers().get(i).getUsername().equals(username)) {
                throw new ApiException("User already exists in group");
            }
        }

        if (user.getGroups() == null) {
            user.setGroups(new ArrayList<>());
        }
        user.getGroups().add(group);

        if (group.getMembers() == null) {
            group.setMembers(new ArrayList<>());
        }
        group.getMembers().add(user);

        userRepository.save(user);
        return ApiHelper.convertGroupToGroupResponse(groupRepository.save(group));
    }

    @Override
    public void removeUserFromGroup(String groupName, String username)
            throws ApiException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User " + username + " not found"));
        Group group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new ApiException("Group " + groupName + " not found"));
        if (!group.getMembers().contains(user)) {
            throw new ApiException("User is not part of the group");
        }

        user.getGroups().remove(group);
        group.getMembers().remove(user);

        userRepository.save(user);
        groupRepository.save(group);
    }
}
