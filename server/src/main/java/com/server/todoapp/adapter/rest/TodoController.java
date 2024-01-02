package com.server.todoapp.adapter.rest;

import com.server.todoapp.application.TodoService;
import com.server.todoapp.domain.dto.TodoPostRequest;
import com.server.todoapp.domain.dto.TodoResponse;
import com.server.todoapp.domain.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
@Transactional
@CrossOrigin(origins = "*", maxAge = 3600)
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/add")
    public ResponseEntity<TodoResponse> addTodo(@RequestBody TodoPostRequest request)
            throws UserNotFoundException {
        return new ResponseEntity<>(todoService.insertTodo(request), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TodoResponse>> getAllTodos(@RequestParam("username") String username)
            throws UserNotFoundException {
        return new ResponseEntity<>(todoService.getAllTodos(username), HttpStatus.OK);
    }
}
