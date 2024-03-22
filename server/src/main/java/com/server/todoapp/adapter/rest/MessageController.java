package com.server.todoapp.adapter.rest;

import com.server.todoapp.application.MessageService;
import com.server.todoapp.domain.dto.GroupMessage;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@Transactional
@CrossOrigin(origins = "*", maxAge = 3600)
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<GroupMessage>> loadMessagesForGroup(@RequestParam("group") String groupName) {
        return new ResponseEntity<>(messageService.loadAllMessagesForGroup(groupName), HttpStatus.OK);
    }
}
