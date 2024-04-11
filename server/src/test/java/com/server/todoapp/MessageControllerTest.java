package com.server.todoapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.server.todoapp.domain.dto.GroupMessage;
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

import static org.junit.Assert.assertEquals;

public class MessageControllerTest extends ApiTestCase {
    private String messagesApiUrl;

    @Before
    @Override
    public void preparation() {
        messagesApiUrl = API_URL + port + "/messages";
    }

    @Test
    public void testGetAllMessages() throws JsonProcessingException {
        HttpEntity<String> request = new HttpEntity<>("", HEADERS);
        ResponseEntity<String> response =
                restTemplate.exchange(messagesApiUrl + "/all?group=Romanian Informatics Revolution",
                HttpMethod.GET, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<GroupMessage> actualList = OBJECT_MAPPER.readValue(response.getBody(), new TypeReference<>() {});
        assertEquals(5, actualList.size());

        List<String> expectedList = Arrays.asList("Morning everybody. I'm not sure I'll get in time for today's presentation. I'll let you know if that's the case.",
                "I'll be late today. Prepare the presentation until I arrive.",
                "Ok, do you need me to do anything else?",
                "Changed my mind. I don't want to do the presentation right now. Is that ok for you?",
                "No, the presentation will take place as discussed in yesterday's team meeting");
        for (int i = 0; i < expectedList.size(); i++) {
            assertEquals(expectedList.get(i), actualList.get(i).getMessage());
        }
    }

    @Test
    public void testPostMessage() throws JsonProcessingException {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("groupName", "Romanian Informatics Revolution");
        requestBody.put("username", "Edi_Pop");
        requestBody.put("message", "New message");
        String requestBodyJson = OBJECT_MAPPER.writeValueAsString(requestBody);

        HttpEntity<String> request = new HttpEntity<>(requestBodyJson, HEADERS);
        ResponseEntity<String> response = restTemplate.exchange(messagesApiUrl + "/add",
                HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        GroupMessage message = OBJECT_MAPPER.readValue(response.getBody(), GroupMessage.class);
        assertEquals("New message", message.getMessage());
        assertEquals("Edi Pop", message.getUsername());
    }
}
