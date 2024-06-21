package com.server.todoapp.utils;

import com.server.todoapp.application.api.helper.ApiHelper;
import com.server.todoapp.domain.data.types.Priority;
import com.server.todoapp.domain.data.types.Status;
import com.server.todoapp.domain.entity.*;
import com.server.todoapp.domain.repository.*;
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

    private final GroupRepository groupRepository;

    private final MessageRepository messageRepository;

    private final RatingRepository ratingRepository;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Value("${security.bcrypt.salt}")
    private String bcryptSalt;

    public InitialDataLoader(UserRepository userRepository, TodoRepository todoRepository, GroupRepository groupRepository, MessageRepository messageRepository, RatingRepository ratingRepository) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
        this.groupRepository = groupRepository;
        this.messageRepository = messageRepository;
        this.ratingRepository = ratingRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        if (ddlAuto.equals("create")) {
            User userEdiPop = createUser("Edi_Pop",
                    "edi.pop43@gmail.com",
                    BCrypt.hashpw("AppAdmin123@", bcryptSalt));

            User userVoicuAna = createUser("Voicu_Ana",
                    "voicuana1996@gmail.com",
                    BCrypt.hashpw("AppAdminVoicuAna@1", bcryptSalt));

            User userAndreiVasile = createUser("Andrei_Vasile",
                    "andrei.vasile@gmail.com",
                    BCrypt.hashpw("AppAdminVasile5643&", bcryptSalt));

            User userDobreCosmin = createUser("Dobre_Cosmin",
                    "dobre_cosmin@gmail.com",
                    BCrypt.hashpw("AppAdminCosmin5643&", bcryptSalt));

            User userMariaAndone = createUser("Maria_Andone",
                    "andone.mariaUU22@gmail.com",
                    BCrypt.hashpw("AppMariaAdimn54&", bcryptSalt));

            User userTudoseVera = createUser("Vera_Tudose",
                    "tudose.MARIA19@gmail.com",
                    BCrypt.hashpw("TudoseAdmin53&", bcryptSalt));

            Todo todoFixDescriptionDisplay = createTodo("Fix the description display",
                    "The display of the description of the todo does not show all the text. Please take a look and fix.",
                    userEdiPop,
                    "18.10.2024",
                    Status.OPEN,
                    Priority.PRIO_1);
            Todo todoImplementGetAllTodosEndpoint = createTodo("Implement GET HTTP method get all todos endpoint",
                    "The endpoint must use GET HTTP method to return all todos for a specified user. The user id must be sent as query parameter to this endpoint.",
                    userEdiPop,
                    "20.08.2024",
                    Status.CLOSED,
                    Priority.PRIO_2);
            Todo todoImplementSortingFunction = createTodo("Implement sorting function",
                    "Implement a sorting function to sort all todos by the due date field",
                    userEdiPop,
                    "25.09.2024",
                    Status.IN_PROGRESS,
                    Priority.PRIO_1);
            Todo todoDatabaseDesign = createTodo("Database Design",
                    "Design the database schema for storing user credentials, profiles, roles, and " +
                            "permissions. Ensure the design supports scalability and security best practices, " +
                            "such as encryption of sensitive data.",
                    userEdiPop,
                    "30.10.2024",
                    Status.IN_PROGRESS,
                    Priority.PRIO_1);
            Todo todoApiDevelopment = createTodo("API Development",
                    "Develop APIs for user registration, login, password management, and account recovery." +
                            " Ensure APIs follow RESTful principles and include proper authentication and authorization " +
                            "mechanisms.",
                    userEdiPop,
                    "30.10.2024",
                    Status.IN_PROGRESS,
                    Priority.PRIO_1);
            Todo todoUserInterfaceDesign = createTodo("User Interface Design",
                    "Design the user interfaces for the authentication system, including login forms, " +
                            "registration forms, and user management dashboards. Focus on usability and security, " +
                            "such as preventing phishing attacks.",
                    userEdiPop,
                    "31.12.2024",
                    Status.OPEN,
                    Priority.PRIO_2);
            Todo todoSecurityImplementation = createTodo("Security Implementation",
                    "Implement security measures such as password hashing, multi-factor authentication " +
                            "(MFA), rate limiting, and protection against common threats like SQL injection and " +
                            "cross-site scripting (XSS).",
                    userEdiPop,
                    "31.12.2024",
                    Status.OPEN,
                    Priority.PRIO_2);

            Rating rating1 = createRating(1.0);
            rating1.setTodo(todoImplementGetAllTodosEndpoint);
            rating1.setUser(userAndreiVasile);
            todoImplementGetAllTodosEndpoint.getRatings().add(rating1);

            Rating rating2 = createRating(3.0);
            rating2.setTodo(todoImplementGetAllTodosEndpoint);
            rating2.setUser(userDobreCosmin);
            todoImplementGetAllTodosEndpoint.getRatings().add(rating2);

            Group group1 = createGroup("Romanian Informatics Revolution");
            group1.getMembers().add(userEdiPop);
            group1.getMembers().add(userVoicuAna);
            group1.getMembers().add(userDobreCosmin);

            Group group2 = createGroup("Indian Geeks");
            group2.getMembers().add(userTudoseVera);

            Group group3 = createGroup("Brainstorm Brigade");
            group3.getMembers().add(userMariaAndone);
            group3.getMembers().add(userDobreCosmin);

            Group group4 = createGroup("Task Titans");
            group4.getMembers().add(userAndreiVasile);
            group4.getMembers().add(userEdiPop);

            Group group5 = createGroup("Efficiency Experts");
            group5.getMembers().add(userEdiPop);
            group5.getMembers().add(userMariaAndone);
            group5.getMembers().add(userTudoseVera);
            group5.getMembers().add(userVoicuAna);

            Group group6 = createGroup("Productivity Powerhouse");
            group6.getMembers().add(userAndreiVasile);
            group6.getMembers().add(userDobreCosmin);
            group6.getMembers().add(userMariaAndone);

            Group group7 = createGroup("Project Pioneers");
            group7.getMembers().add(userVoicuAna);
            group7.getMembers().add(userTudoseVera);

            Message msg1 = createMessage("I'll be late today. Prepare the presentation until I arrive.",
                    userEdiPop,
                    "01-12-2018 09:45:03");
            msg1.setGroup(group1);
            group1.getMessages().add(msg1);
            userEdiPop.getMessages().add(msg1);

            Message msg2 = createMessage("Ok, do you need me to do anything else?",
                    userVoicuAna,
                    "01-12-2018 09:50:18");
            msg2.setGroup(group1);
            group1.getMessages().add(msg2);
            userVoicuAna.getMessages().add(msg2);

            Message msg3 = createMessage("Changed my mind. I don't want to do the presentation right now. Is that ok for you?",
                    userVoicuAna,
                    "01-12-2018 09:51:33");
            msg3.setGroup(group1);
            group1.getMessages().add(msg3);
            userVoicuAna.getMessages().add(msg3);

            Message msg4 = createMessage("No, the presentation will take place as discussed in yesterday's team meeting",
                    userEdiPop,
                    "01-12-2018 09:55:06");
            msg4.setGroup(group1);
            group1.getMessages().add(msg4);
            userEdiPop.getMessages().add(msg4);

            Message msg5 = createMessage("Morning everybody. I'm not sure I'll get in time for today's presentation. I'll let you know if that's the case.",
                    userEdiPop,
                    "01-12-2018 09:13:57");
            msg5.setGroup(group1);
            group1.getMessages().add(msg5);
            userEdiPop.getMessages().add(msg5);

            userEdiPop.getTodos().add(todoFixDescriptionDisplay);
            userEdiPop.getTodos().add(todoImplementGetAllTodosEndpoint);
            userEdiPop.getTodos().add(todoImplementSortingFunction);
            userEdiPop.getTodos().add(todoDatabaseDesign);
            userEdiPop.getTodos().add(todoApiDevelopment);
            userEdiPop.getTodos().add(todoUserInterfaceDesign);
            userEdiPop.getTodos().add(todoSecurityImplementation);

            userRepository.save(userEdiPop);
            userRepository.save(userVoicuAna);
            userRepository.save(userAndreiVasile);
            userRepository.save(userDobreCosmin);
            userRepository.save(userMariaAndone);
            userRepository.save(userTudoseVera);

            todoRepository.save(todoFixDescriptionDisplay);
            todoRepository.save(todoImplementGetAllTodosEndpoint);
            todoRepository.save(todoImplementSortingFunction);
            todoRepository.save(todoDatabaseDesign);
            todoRepository.save(todoApiDevelopment);
            todoRepository.save(todoUserInterfaceDesign);
            todoRepository.save(todoSecurityImplementation);

            groupRepository.save(group1);
            groupRepository.save(group2);
            groupRepository.save(group3);
            groupRepository.save(group4);
            groupRepository.save(group5);
            groupRepository.save(group6);
            groupRepository.save(group7);

            messageRepository.save(msg1);
            messageRepository.save(msg2);

            ratingRepository.save(rating1);
            ratingRepository.save(rating2);
        }
    }

    private User createUser(String username, String email, String hashPassword) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(hashPassword);
        user.setUsername(username);
        user.setTodos(new ArrayList<>());
        user.setMessages(new ArrayList<>());

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
        todo.setRatings(new ArrayList<>());

        return todo;
    }

    private Group createGroup(String groupName) {
        Group group = new Group();
        group.setGroupName(groupName);
        group.setMembers(new ArrayList<>());
        group.setMessages(new ArrayList<>());

        return group;
    }

    private Message createMessage(String messageText, User user, String dateTime) {
        Message message = new Message();
        message.setMessage(messageText);
        message.setUser(user);
        message.setDateTime(DateUtils.parseDateTime(dateTime));

        return message;
    }

    private Rating createRating(Double value) {
        Rating rating = new Rating();
        rating.setRating(value);

        return rating;
    }
}
