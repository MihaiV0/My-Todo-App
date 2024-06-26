package com.server.todoapp.adapter.rest;

import com.server.todoapp.application.TodoService;
import com.server.todoapp.domain.dto.TodoPatchRequest;
import com.server.todoapp.domain.dto.TodoPostRequest;
import com.server.todoapp.domain.dto.TodoResponse;
import com.server.todoapp.domain.exception.ApiException;
import com.server.todoapp.domain.exception.TodoNotFoundException;
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

    @PatchMapping("/edit")
    public ResponseEntity<TodoResponse> updateTodo(@RequestParam("id") Integer todoId,
                                                   @RequestBody TodoPatchRequest request)
            throws TodoNotFoundException {
        return new ResponseEntity<>(todoService.updateTodo(todoId, request), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTodo(@RequestParam("id") Integer todoId)
            throws TodoNotFoundException {
        todoService.deleteTodo(todoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TodoResponse>> searchTodo(@RequestParam("text") String text,
                                                         @RequestParam("username") String username)
            throws UserNotFoundException {
        return new ResponseEntity<>(todoService.searchTodo(text, username), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<TodoResponse> findTodoById(@RequestParam("id") Integer todoId)
            throws ApiException {
        return new ResponseEntity<>(todoService.getTodoById(todoId), HttpStatus.OK);
    }

    @PatchMapping("/rate")
    public ResponseEntity<TodoResponse> rateTodo(@RequestParam("username") String username,
                                                 @RequestParam("todoId") Integer todoId,
                                                 @RequestParam("ratingValue") Double ratingValue)
            throws ApiException {
        return new ResponseEntity<>(todoService.updateRating(username, todoId, ratingValue), HttpStatus.OK);
    }
}
