package com.server.todoapp.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.server.todoapp.domain.data.types.Priority;
import com.server.todoapp.domain.data.types.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer todoId;

    @Column
    private String todoTitle;

    @Column
    private String todoDescription;

    @Column
    private LocalDate dueDate;

    @Column
    private Status status;

    @Column
    private Priority priority;

    @JsonIgnoreProperties("todos")
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Todo(String todoTitle, String todoDescription) {
        this.todoTitle = todoTitle;
        this.todoDescription = todoDescription;
    }
}
