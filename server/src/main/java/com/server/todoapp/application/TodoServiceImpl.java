package com.server.todoapp.application;

import com.server.todoapp.domain.dto.TodoPostRequest;
import com.server.todoapp.domain.dto.TodoResponse;
import com.server.todoapp.domain.entity.Todo;
import com.server.todoapp.domain.entity.User;
import com.server.todoapp.domain.exception.UserNotFoundException;
import com.server.todoapp.domain.repository.TodoRepository;
import com.server.todoapp.domain.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

        Todo todo = new Todo(request.getTitle(), request.getDescription());
        todo.setUser(user);

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

    private List<TodoResponse> convertListOfTodoToListOfTodoResponse(List<Todo> allTodos) {
        List<TodoResponse> todoResponses = new ArrayList<>();
        for (int i = 0; i < allTodos.size(); i++) {
            todoResponses.add(modelMapper.map(allTodos.get(i), TodoResponse.class));
        }

        return todoResponses;
    }
}
