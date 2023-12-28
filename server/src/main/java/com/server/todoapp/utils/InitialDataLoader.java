package com.server.todoapp.utils;

import com.server.todoapp.domain.entity.User;
import com.server.todoapp.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.mindrot.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Value("${security.bcrypt.salt}")
    private String bcryptSalt;

    public InitialDataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        if (ddlAuto.equals("create")) {
            User user = createUser("Edi_Pop",
                    "edi.pop43@gmail.com",
                    BCrypt.hashpw("AppAdmin123@", bcryptSalt));
            userRepository.save(user);
        }
    }

    private User createUser(String username, String email, String hashPassword) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(hashPassword);
        user.setUsername(username);

        return user;
    }
}
