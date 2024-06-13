package com.server.todoapp.application;

import com.server.todoapp.application.api.helper.ApiHelper;
import com.server.todoapp.domain.data.types.Priority;
import com.server.todoapp.domain.data.types.Status;
import com.server.todoapp.domain.dto.TodoPatchRequest;
import com.server.todoapp.domain.dto.TodoPostRequest;
import com.server.todoapp.domain.dto.TodoResponse;
import com.server.todoapp.domain.entity.Rating;
import com.server.todoapp.domain.entity.Todo;
import com.server.todoapp.domain.entity.User;
import com.server.todoapp.domain.exception.ApiException;
import com.server.todoapp.domain.exception.TodoNotFoundException;
import com.server.todoapp.domain.exception.UserNotFoundException;
import com.server.todoapp.domain.repository.RatingRepository;
import com.server.todoapp.domain.repository.TodoRepository;
import com.server.todoapp.domain.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TodoServiceImpl implements TodoService {

    private final ModelMapper modelMapper;

    private final TodoRepository todoRepository;

    private final UserRepository userRepository;

    private final RatingRepository ratingRepository;

    public TodoServiceImpl(ModelMapper modelMapper, TodoRepository todoRepository, UserRepository userRepository, RatingRepository ratingRepository) {
        this.modelMapper = modelMapper;
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
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
        todo.setStatus(Status.valueOf(ApiHelper.replaceSpacesWithUnderscores(
                request.getStatus()).toUpperCase()));
        todo.setPriority(Priority.valueOf(ApiHelper.replaceSpacesWithUnderscores(
                request.getPriority()).toUpperCase()));
        todo.setRatings(new ArrayList<>());

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

        return ApiHelper.convertListToDestinationType(todoRepository.getAllTodosForUser(user.getId()), modelMapper,
                Todo.class, TodoResponse.class);
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
            todo.setStatus(Status.valueOf(ApiHelper.replaceSpacesWithUnderscores(
                    request.getStatus()).toUpperCase()));
        }
        if (request.getPriority() != null) {
            todo.setPriority(Priority.valueOf(ApiHelper.replaceSpacesWithUnderscores(
                    request.getPriority()).toUpperCase()));
        }

        return ApiHelper.convertTodoToTodoResponse(todoRepository.save(todo));
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

        return ApiHelper.convertListToDestinationType(todoRepository.searchForTodo(text, user.getId()), modelMapper,
                Todo.class, TodoResponse.class);
    }

    @Override
    public TodoResponse getTodoById(Integer id)
            throws ApiException {
        Todo todo = todoRepository.findByTodoId(id)
                .orElseThrow(() -> new ApiException("Todo not found"));
        return ApiHelper.convertTodoToTodoResponse(todo);
    }

    @Override
    public TodoResponse updateRating(String username, Integer todoId, Double ratingValue)
            throws ApiException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User with username " + username + " not found!"));
        Todo todo = todoRepository.findByTodoId(todoId)
                .orElseThrow(() -> new ApiException("Todo not found"));
        if (Objects.equals(user.getId(), todo.getUser().getId())) {
            throw new ApiException("You can't rate your own todo!");
        }
        for (int i = 0; i < todo.getRatings().size(); i++) {
            if (Objects.equals(user.getId(), todo.getRatings().get(i).getUser().getId())) {
                throw new ApiException("You already rated this todo!");
            }
        }

        Rating rating = new Rating();
        rating.setRating(ratingValue);
        rating.setTodo(todo);
        rating.setUser(user);

        if (user.getRatings() == null) {
            user.setRatings(new ArrayList<>());
        }
        user.getRatings().add(rating);

        if (todo.getRatings() == null) {
            todo.setRatings(new ArrayList<>());
        }
        todo.getRatings().add(rating);

        userRepository.save(user);
        ratingRepository.save(rating);
        return ApiHelper.convertTodoToTodoResponse(todoRepository.save(todo));
    }
}
