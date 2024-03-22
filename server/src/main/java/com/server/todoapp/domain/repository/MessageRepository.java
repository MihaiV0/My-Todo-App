package com.server.todoapp.domain.repository;

import com.server.todoapp.domain.entity.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {

    @Query(value = "Select * FROM message WHERE group_id = :groupId", nativeQuery = true)
    List<Message> findByGroupId(@Param("groupId") Integer groupId);
}
