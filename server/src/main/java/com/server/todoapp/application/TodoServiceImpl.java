package com.server.todoapp.application;

import com.server.todoapp.domain.data.types.Priority;
import com.server.todoapp.domain.data.types.Status;
import com.server.todoapp.domain.dto.TodoPatchRequest;
import com.server.todoapp.domain.dto.TodoPostRequest;
import com.server.todoapp.domain.dto.TodoResponse;
import com.server.todoapp.domain.entity.Todo;
import com.server.todoapp.domain.entity.User;
import com.server.todoapp.domain.exception.TodoNotFoundException;
import com.server.todoapp.domain.exception.UserNotFoundException;
import com.server.todoapp.domain.repository.TodoRepository;
import com.server.todoapp.domain.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    private final ModelMapper modelMapper;

    private final TodoRepository todoRepository;

    private final UserRepository userRepository;

    public TodoServiceImpl(ModelMapper modelMapper, TodoRepository todoRepository, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TodoResponse insertTodo(TodoPostRequest request)
            throws UserNotFoundException {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User with username " + request.getUsername() + " not found!"));

        Todo todo = new Todo();
        todo.setTodoTitle(request.getTitle());
        todo.setTodoDescription(request.getDescription());
        todo.setUser(user);
        todo.setDueDate(LocalDate.parse(request.getDueDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        todo.setStatus(Status.valueOf(replaceSpacesWithUnderscores(request.getStatus()).toUpperCase()));
        todo.setPriority(Priority.valueOf(replaceSpacesWithUnderscores(request.getPriority()).toUpperCase()));

        if (user.getTodos() == null) {
            user.setTodos(new ArrayList<>());
        }
        user.getTodos().add(todo);

        return modelMapper.map(todoRepository.save(todo), TodoResponse.class);
    }

    @Override
    public List<TodoResponse> getAllTodos(String username)
            throws UserNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found!"));

        return convertListOfTodoToListOfTodoResponse(todoRepository.getAllTodosForUser(user.getId()));
    }

    @Override
    public TodoResponse updateTodo(Integer todoId, TodoPatchRequest request)
            throws TodoNotFoundException {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoNotFoundException("Todo with id " + todoId + " not found!"));

        if (request.getTitle() != null) {
            todo.setTodoTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            todo.setTodoDescription(request.getDescription());
        }
        if (request.getDueDate() != null) {
            todo.setDueDate(LocalDate.parse(request.getDueDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        }
        if (request.getStatus() != null) {
            todo.setStatus(Status.valueOf(replaceSpacesWithUnderscores(request.getStatus()).toUpperCase()));
        }
        if (request.getPriority() != null) {
            todo.setPriority(Priority.valueOf(replaceSpacesWithUnderscores(request.getPriority()).toUpperCase()));
        }

        return modelMapper.map(todoRepository.save(todo), TodoResponse.class);
    }

    @Override
    public void deleteTodo(Integer todoId)
            throws TodoNotFoundException {
        todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoNotFoundException("Todo with id " + todoId + " not found!"));

        todoRepository.deleteById(todoId);
    }

    @Override
    public List<TodoResponse> searchTodo(String text, String username)
            throws UserNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found!"));

        return convertListOfTodoToListOfTodoResponse(todoRepository.searchForTodo(text, user.getId()));
    }

    private List<TodoResponse> convertListOfTodoToListOfTodoResponse(List<Todo> allTodos) {
        List<TodoResponse> todoResponses = new ArrayList<>();
        for (int i = 0; i < allTodos.size(); i++) {
            todoResponses.add(modelMapper.map(allTodos.get(i), TodoResponse.class));
        }

        return todoResponses;
    }

    private String replaceSpacesWithUnderscores(String initial) {
        StringBuilder sb = new StringBuilder();
        String[] parts = initial.split(" ");
        sb.append(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            sb.append("_");
            sb.append(parts[i]);
        }
        return sb.toString();
    }
}
