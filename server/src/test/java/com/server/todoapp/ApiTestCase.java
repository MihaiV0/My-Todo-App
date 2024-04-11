package com.server.todoapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.todoapp.domain.repository.GroupRepository;
import com.server.todoapp.domain.repository.MessageRepository;
import com.server.todoapp.domain.repository.TodoRepository;
import com.server.todoapp.domain.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ApiTestCase {
//    @Autowired
    protected RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private CommandLineRunner commandLineRunner;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private MessageRepository messageRepository;
    @LocalServerPort
    protected int port;
    protected final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    protected final String API_URL = "http://localhost:";
    protected final HttpHeaders HEADERS = new HttpHeaders();

    protected ApiTestCase() {
        HEADERS.setContentType(MediaType.APPLICATION_JSON);
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);
    }

    @Before
    public void preparation() {
    }

    @After
    public void revertDatabase() throws Exception {
        groupRepository.deleteAll();
        userRepository.deleteAll();
        todoRepository.deleteAll();
        messageRepository.deleteAll();
        commandLineRunner.run();
    }
}
