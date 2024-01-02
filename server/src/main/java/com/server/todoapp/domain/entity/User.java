package com.server.todoapp.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "\"user\"",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_username",
                        columnNames = "username"
                ),
                @UniqueConstraint(
                        name = "unique_email",
                        columnNames = "email"
                )
        }
)
@Entity
@ToString
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @JsonIgnoreProperties("user")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Todo> todos;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
