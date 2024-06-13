package com.server.todoapp.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ratingId;

    @Column
    private Double rating;

    @JsonIgnoreProperties("ratings")
    @ManyToOne
    @JoinColumn(name = "todo_id", referencedColumnName = "todoId")
    private Todo todo;

    @JsonIgnoreProperties("ratings")
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
