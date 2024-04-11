package com.server.todoapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.todoapp.domain.dto.UserResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UserControllerTest extends ApiTestCase {
    private String usersApiUrl;
    @Value("${security.bcrypt.salt}")
    private String bcryptSalt;

    @Before
    @Override
    public void preparation() {
        usersApiUrl = API_URL + port + "/users";
    }

    @Test
    public void testRegisterUser() throws JsonProcessingException {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", "email_user@gmail.com");
        requestBody.put("username", "new_user");
        requestBody.put("password", "AppAdmin1224$#gA");
        String requestBodyJson = OBJECT_MAPPER.writeValueAsString(requestBody);

        HttpEntity<String> request = new HttpEntity<>(requestBodyJson, HEADERS);
        ResponseEntity<String> response = restTemplate.exchange(usersApiUrl + "/register",
                HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        UserResponseDto userResponseDto = OBJECT_MAPPER.readValue(response.getBody(), UserResponseDto.class);
        assertEquals("email_user@gmail.com", userResponseDto.getEmail());
        assertEquals("new_user", userResponseDto.getUsername());
    }

    @Test
    public void testLoginUser() throws JsonProcessingException {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("usernameOrEmail", "Edi_Pop");
        requestBody.put("password", BCrypt.hashpw("AppAdmin123@", bcryptSalt));
        String requestBodyJson = OBJECT_MAPPER.writeValueAsString(requestBody);

        HttpEntity<String> request = new HttpEntity<>(requestBodyJson, HEADERS);
        ResponseEntity<String> response = restTemplate.exchange(usersApiUrl + "/login",
                HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserResponseDto userResponseDto = OBJECT_MAPPER.readValue(response.getBody(), UserResponseDto.class);
        assertEquals("edi.pop43@gmail.com", userResponseDto.getEmail());
        assertEquals("Edi_Pop", userResponseDto.getUsername());
    }
}
