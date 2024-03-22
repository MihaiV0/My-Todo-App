package com.server.todoapp.domain.repository;

import com.server.todoapp.domain.entity.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends CrudRepository<Group, Integer> {

    @Query(value = "SELECT * FROM \"group\"", nativeQuery = true)
    List<Group> getAllGroups();

    Optional<Group> findByGroupName(String groupName);
}
