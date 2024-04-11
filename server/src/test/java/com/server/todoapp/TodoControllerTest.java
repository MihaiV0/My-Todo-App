package com.server.todoapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.server.todoapp.domain.data.types.Priority;
import com.server.todoapp.domain.data.types.Status;
import com.server.todoapp.domain.dto.TodoResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TodoControllerTest extends ApiTestCase {
    private String todosApiUrl;

    @Before
    @Override
    public void preparation() {
        todosApiUrl = API_URL + port + "/todos";
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    @Test
    public void testAddTodo() throws JsonProcessingException {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("title", "some title");
        requestBody.put("description", "description text");
        requestBody.put("username", "Edi_Pop");
        requestBody.put("dueDate", "22.01.2024");
        requestBody.put("status", "open");
        requestBody.put("priority", "prio 2");
        String requestBodyJson = OBJECT_MAPPER.writeValueAsString(requestBody);

        HttpEntity<String> request = new HttpEntity<>(requestBodyJson, HEADERS);
        ResponseEntity<String> response = restTemplate.exchange(todosApiUrl + "/add",
                HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        TodoResponse todoResponse = OBJECT_MAPPER.readValue(response.getBody(), TodoResponse.class);
        assertEquals("description text", todoResponse.getDescription());
        assertEquals("2024-01-22", todoResponse.getDueDate().toString());
        assertEquals(Priority.PRIO_2, todoResponse.getPriority());
        assertEquals(Status.OPEN, todoResponse.getStatus());
        assertEquals("some title", todoResponse.getTitle());
    }

    @Test
    public void testFindTodoById() throws JsonProcessingException {
        HttpEntity<String> request = new HttpEntity<>("", HEADERS);
        ResponseEntity<String> response = restTemplate.exchange(todosApiUrl + "/get?id=1",
                HttpMethod.GET, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        TodoResponse todoResponse = OBJECT_MAPPER.readValue(response.getBody(), TodoResponse.class);
        assertEquals("The display of the description of the todo does not show all the text. Please take a look and fix.",
                todoResponse.getDescription());
        assertEquals("2024-10-18", todoResponse.getDueDate().toString());
        assertEquals(Priority.PRIO_1, todoResponse.getPriority());
        assertEquals(Status.OPEN, todoResponse.getStatus());
        assertEquals("Fix the description display", todoResponse.getTitle());
        assertTrue(todoResponse.getTodoId().equals(1));
    }

    @Test
    public void testDeleteTodo() throws JsonProcessingException {
        HttpEntity<String> request = new HttpEntity<>("", HEADERS);
        ResponseEntity<String> response = restTemplate.exchange(todosApiUrl + "/delete?id=1",
                HttpMethod.DELETE, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        try {
            restTemplate.exchange(todosApiUrl + "/get?id=1", HttpMethod.GET, request, String.class);
            fail("This should have thrown an exception");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertEquals("400 : \"Todo not found\"", e.getMessage());
        }
    }

    @Test
    public void testEditTodo() throws JsonProcessingException {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("title", "some title");
        requestBody.put("description", "description text");
        requestBody.put("dueDate", "22.01.2024");
        requestBody.put("status", "open");
        requestBody.put("priority", "prio 2");
        String requestBodyJson = OBJECT_MAPPER.writeValueAsString(requestBody);

        HttpEntity<String> request = new HttpEntity<>(requestBodyJson, HEADERS);
        ResponseEntity<String> response = restTemplate.exchange(todosApiUrl + "/edit?id=1",
                HttpMethod.PATCH, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        TodoResponse todoResponse = OBJECT_MAPPER.readValue(response.getBody(), TodoResponse.class);
        assertEquals("description text", todoResponse.getDescription());
        assertEquals("2024-01-22", todoResponse.getDueDate().toString());
        assertEquals(Priority.PRIO_2, todoResponse.getPriority());
        assertEquals(Status.OPEN, todoResponse.getStatus());
        assertEquals("some title", todoResponse.getTitle());
    }

    @Test
    public void testGetAllTodos() throws JsonProcessingException {
        HttpEntity<String> request = new HttpEntity<>("", HEADERS);
        ResponseEntity<String> response = restTemplate.exchange(todosApiUrl + "/all?username=Edi_Pop",
                HttpMethod.GET, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<TodoResponse> actualList = OBJECT_MAPPER.readValue(response.getBody(), new TypeReference<>() {});
        assertEquals(3, actualList.size());

        List<TodoResponse> expectedList = createList();
        for (int i = 0; i < expectedList.size(); i++) {
            assertEquals(expectedList.get(i).getDescription(), actualList.get(i).getDescription());
            assertEquals(expectedList.get(i).getPriority(), actualList.get(i).getPriority());
            assertEquals(expectedList.get(i).getStatus(), actualList.get(i).getStatus());
            assertEquals(expectedList.get(i).getTitle(), actualList.get(i).getTitle());
            assertEquals(expectedList.get(i).getDueDate(), actualList.get(i).getDueDate());
        }
    }

    @Test
    public void testSearchTodos() throws JsonProcessingException {
        HttpEntity<String> request = new HttpEntity<>("", HEADERS);
        ResponseEntity<String> response = restTemplate.exchange(todosApiUrl + "/search?username=Edi_Pop&text=impl",
                HttpMethod.GET, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<TodoResponse> actualList = OBJECT_MAPPER.readValue(response.getBody(), new TypeReference<>() {});
        assertEquals(2, actualList.size());

        List<TodoResponse> expectedList = createSearchList();
        for (int i = 0; i < expectedList.size(); i++) {
            assertEquals(expectedList.get(i).getDescription(), actualList.get(i).getDescription());
            assertEquals(expectedList.get(i).getPriority(), actualList.get(i).getPriority());
            assertEquals(expectedList.get(i).getStatus(), actualList.get(i).getStatus());
            assertEquals(expectedList.get(i).getTitle(), actualList.get(i).getTitle());
            assertEquals(expectedList.get(i).getDueDate(), actualList.get(i).getDueDate());
        }
    }

    private List<TodoResponse> createList() {
        List<TodoResponse> list = new ArrayList<>();
        TodoResponse todoFixDescriptionDisplay = createTodo("Fix the description display",
                "The display of the description of the todo does not show all the text. Please take a look and fix.",
                "18.10.2024",
                Status.OPEN,
                Priority.PRIO_1);
        TodoResponse todoImplementGetAllTodosEndpoint = createTodo("Implement GET HTTP method get all todos endpoint",
                "The endpoint must use GET HTTP method to return all todos for a specified user. The user id must be sent as query parameter to this endpoint.",
                "07.05.2024",
                Status.CLOSED,
                Priority.PRIO_2);
        TodoResponse todoImplementSortingFunction = createTodo("Implement sorting function",
                "Implement a sorting function to sort all todos by the due date field",
                "25.08.2024",
                Status.IN_PROGRESS,
                Priority.PRIO_1);
        list.add(todoFixDescriptionDisplay);
        list.add(todoImplementGetAllTodosEndpoint);
        list.add(todoImplementSortingFunction);

        return list;
    }

    private List<TodoResponse> createSearchList() {
        List<TodoResponse> list = new ArrayList<>();
        TodoResponse todoImplementGetAllTodosEndpoint = createTodo("Implement GET HTTP method get all todos endpoint",
                "The endpoint must use GET HTTP method to return all todos for a specified user. The user id must be sent as query parameter to this endpoint.",
                "07.05.2024",
                Status.CLOSED,
                Priority.PRIO_2);
        TodoResponse todoImplementSortingFunction = createTodo("Implement sorting function",
                "Implement a sorting function to sort all todos by the due date field",
                "25.08.2024",
                Status.IN_PROGRESS,
                Priority.PRIO_1);
        list.add(todoImplementGetAllTodosEndpoint);
        list.add(todoImplementSortingFunction);

        return list;
    }

    private TodoResponse createTodo(String title,
                            String description,
                            String dueDate,
                            Status status,
                            Priority priority) {
        TodoResponse todo = new TodoResponse();
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setDueDate(LocalDate.parse(dueDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        todo.setStatus(status);
        todo.setPriority(priority);

        return todo;
    }
}
