package com.server.todoapp.domain.repository;

import com.server.todoapp.domain.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM \"user\" WHERE username = :usernameOrEmail OR email = :usernameOrEmail", nativeQuery = true)
    Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);
}
