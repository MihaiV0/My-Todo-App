package com.server.todoapp.domain.repository;

import com.server.todoapp.domain.entity.Todo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends CrudRepository<Todo, Integer> {

    @Query(value = "SELECT * FROM todo WHERE user_id = :userId", nativeQuery = true)
    List<Todo> getAllTodosForUser(@Param("userId") Integer userId);

    @Query(value = "SELECT * FROM todo WHERE user_id = :userId AND LOWER(todo_title) LIKE LOWER('%' || :text || '%')", nativeQuery = true)
    List<Todo> searchForTodo(@Param("text") String text, @Param("userId") Integer userId);

    Optional<Todo> findByTodoId(Integer todoId);
}
