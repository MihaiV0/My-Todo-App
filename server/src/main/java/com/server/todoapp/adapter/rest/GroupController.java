package com.server.todoapp.adapter.rest;

import com.server.todoapp.application.GroupService;
import com.server.todoapp.domain.dto.GroupPostRequest;
import com.server.todoapp.domain.dto.GroupResponse;
import com.server.todoapp.domain.exception.ApiException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
@Transactional
@CrossOrigin(origins = "*", maxAge = 3600)
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/add")
    public ResponseEntity<GroupResponse> insertGroup(@RequestBody GroupPostRequest request,
                                                     @RequestParam("username") String username)
            throws ApiException {
        return new ResponseEntity<>(groupService.insertGroup(username, request), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GroupResponse>> getAllGroups() {
        return new ResponseEntity<>(groupService.getAllGroups(), HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<GroupResponse> addUserToGroup(@RequestParam("username") String username,
                                                        @RequestParam("groupName") String groupName)
            throws ApiException {
        return new ResponseEntity<>(groupService.addUserToGroup(groupName, username), HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeUserFromGroup(@RequestParam("username") String username,
                                                    @RequestParam("groupName") String groupName)
            throws ApiException {
        groupService.removeUserFromGroup(groupName, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
