package com.server.todoapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.server.todoapp.domain.dto.GroupResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GroupControllerTest extends ApiTestCase {
    private String groupsApiUrl;

    @Before
    @Override
    public void preparation() {
        groupsApiUrl = API_URL + port + "/groups";
    }

    @Test
    public void testGetAllGroups() throws JsonProcessingException {
        HttpEntity<String> request = new HttpEntity<>("", HEADERS);
        ResponseEntity<String> response = restTemplate.exchange(groupsApiUrl + "/all",
                HttpMethod.GET, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<GroupResponse> actualList = OBJECT_MAPPER.readValue(response.getBody(), new TypeReference<>() {});
        assertEquals(7, actualList.size());

        List<String> expectedList = Arrays.asList("Romanian Informatics Revolution",
                "Indian Geeks", "Brainstorm Brigade", "Task Titans", "Efficiency Experts",
                "Productivity Powerhouse", "Project Pioneers");
        for (int i = 0; i < expectedList.size(); i++) {
            assertEquals(expectedList.get(i), actualList.get(i).getGroupName());
        }
    }

    @Test
    public void testPostGroup() throws JsonProcessingException {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("groupName", "edi doarme");
        String requestBodyJson = OBJECT_MAPPER.writeValueAsString(requestBody);

        HttpEntity<String> request = new HttpEntity<>(requestBodyJson, HEADERS);
        ResponseEntity<String> response = restTemplate.exchange(groupsApiUrl + "/add?username=Edi_Pop",
                HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        GroupResponse groupResponse = OBJECT_MAPPER.readValue(response.getBody(), GroupResponse.class);
        assertEquals("edi doarme", groupResponse.getGroupName());
    }

    @Test
    public void testAddUserToGroup() throws JsonProcessingException {
        HttpEntity<String> request = new HttpEntity<>("", HEADERS);
        ResponseEntity<String> response =
                restTemplate.exchange(groupsApiUrl + "/join?username=Edi_Pop&groupName=Indian Geeks",
                HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        GroupResponse groupResponse = OBJECT_MAPPER.readValue(response.getBody(), GroupResponse.class);
        assertEquals("Indian Geeks", groupResponse.getGroupName());
        assertTrue(groupResponse.getMembers().contains("Edi_Pop"));
    }

    @Test
    public void testRemoveUserFromGroup() throws JsonProcessingException {
        HttpEntity<String> request = new HttpEntity<>("", HEADERS);
        ResponseEntity<String> response =
                restTemplate.exchange(groupsApiUrl + "/remove?username=Edi_Pop&groupName=Task Titans",
                        HttpMethod.DELETE, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<String> responseList = restTemplate.exchange(groupsApiUrl + "/all",
                HttpMethod.GET, request, String.class);

        List<GroupResponse> groupList = OBJECT_MAPPER.readValue(responseList.getBody(), new TypeReference<>() {});
        GroupResponse groupResponse = groupList.stream()
                .filter(group -> group.getGroupName().equals("Task Titans"))
                .findFirst()
                .get();
        assertFalse(groupResponse.getMembers().contains("Edi_Pop"));
    }
}
