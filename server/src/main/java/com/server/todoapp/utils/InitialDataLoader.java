package com.server.todoapp.utils;

import com.server.todoapp.domain.data.types.Priority;
import com.server.todoapp.domain.data.types.Status;
import com.server.todoapp.domain.entity.Todo;
import com.server.todoapp.domain.entity.User;
import com.server.todoapp.domain.repository.TodoRepository;
import com.server.todoapp.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.mindrot.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Component
public class InitialDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    private final TodoRepository todoRepository;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Value("${security.bcrypt.salt}")
    private String bcryptSalt;

    public InitialDataLoader(UserRepository userRepository, TodoRepository todoRepository) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        if (ddlAuto.equals("create")) {
            User userEdiPop = createUser("Edi_Pop",
                    "edi.pop43@gmail.com",
                    BCrypt.hashpw("AppAdmin123@", bcryptSalt));

            Todo todoFixDescriptionDisplay = createTodo("Fix the description display",
                    "The display of the description of the todo does not show all the text. Please take a look and fix.",
                    userEdiPop,
                    "18.10.2024",
                    Status.OPEN,
                    Priority.PRIO_1);
            Todo todoImplementGetAllTodosEndpoint = createTodo("Implement GET HTTP method get all todos endpoint",
                    "The endpoint must use GET HTTP method to return all todos for a specified user. The user id must be sent as query parameter to this endpoint.",
                    userEdiPop,
                    "07.05.2024",
                    Status.CLOSED,
                    Priority.PRIO_2);
            Todo todoImplementSortingFunction = createTodo("Implement sorting function",
                    "Implement a sorting function to sort all todos by the due date field",
                    userEdiPop,
                    "25.08.2024",
                    Status.IN_PROGRESS,
                    Priority.PRIO_1);

            userEdiPop.getTodos().add(todoFixDescriptionDisplay);
            userEdiPop.getTodos().add(todoImplementGetAllTodosEndpoint);
            userEdiPop.getTodos().add(todoImplementSortingFunction);

            userRepository.save(userEdiPop);

            todoRepository.save(todoFixDescriptionDisplay);
            todoRepository.save(todoImplementGetAllTodosEndpoint);
            todoRepository.save(todoImplementSortingFunction);
        }
    }

    private User createUser(String username, String email, String hashPassword) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(hashPassword);
        user.setUsername(username);
        user.setTodos(new ArrayList<>());

        return user;
    }

    private Todo createTodo(String title,
                            String description,
                            User user,
                            String dueDate,
                            Status status,
                            Priority priority) {
        Todo todo = new Todo();
        todo.setTodoTitle(title);
        todo.setTodoDescription(description);
        todo.setUser(user);
        todo.setDueDate(LocalDate.parse(dueDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        todo.setStatus(status);
        todo.setPriority(priority);

        return todo;
    }
}
