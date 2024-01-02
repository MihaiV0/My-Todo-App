package com.server.todoapp.domain.repository;

import com.server.todoapp.domain.entity.Todo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends CrudRepository<Todo, Integer> {

    @Query(value = "SELECT * FROM todo WHERE user_id = :userId", nativeQuery = true)
    List<Todo> getAllTodosForUser(@Param("userId") Integer userId);
}
