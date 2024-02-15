package com.server.todoapp.application.todo.helper;

import com.server.todoapp.application.TodoServiceImpl;
import com.server.todoapp.domain.dto.TodoResponse;
import com.server.todoapp.domain.entity.Todo;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for {@link TodoServiceImpl TodoService}
 */
public class TodoHelper {
    /**
     * Creates a list with items of type {@code TodoResponse} with the items from {@code allTodos}
     * @param allTodos The list with items of type {@code Todo}
     * @param modelMapper The object used to convert items from type {@code Todo} to type {@code TodoResponse}
     * @return A list with items of type {@code TodoResponse}
     * @see TodoResponse
     * @see Todo
     * @see ModelMapper
     */
    public static List<TodoResponse> convertListOfTodoToListOfTodoResponse(List<Todo> allTodos,
                                                                           final ModelMapper modelMapper) {
        List<TodoResponse> todoResponses = new ArrayList<>();
        for (int i = 0; i < allTodos.size(); i++) {
            todoResponses.add(modelMapper.map(allTodos.get(i), TodoResponse.class));
        }

        return todoResponses;
    }

    /**
     * Replaces white spaces with underscores in {@code initial}. Used <b>only</b> when creating enum types from strings
     * @param initial Value used in enum type
     * @return A string which has underscores instead of white spaces
     */
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
}
