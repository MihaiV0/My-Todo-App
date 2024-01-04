package com.server.todoapp.application;

import com.server.todoapp.domain.dto.TodoPatchRequest;
import com.server.todoapp.domain.dto.TodoPostRequest;
import com.server.todoapp.domain.dto.TodoResponse;
import com.server.todoapp.domain.entity.Todo;
import com.server.todoapp.domain.exception.TodoNotFoundException;
import com.server.todoapp.domain.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TodoService {

    TodoResponse insertTodo(TodoPostRequest request) throws UserNotFoundException;

    List<TodoResponse> getAllTodos(String username) throws UserNotFoundException;

    TodoResponse updateTodo(Integer todoId, TodoPatchRequest request) throws TodoNotFoundException;

    void deleteTodo(Integer todoId) throws TodoNotFoundException;

    List<TodoResponse> searchTodo(String text, String username) throws UserNotFoundException;
}
