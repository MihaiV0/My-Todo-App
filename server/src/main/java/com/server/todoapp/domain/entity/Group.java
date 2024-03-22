package com.server.todoapp.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "\"group\"",
        uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_group_name",
                columnNames = "group_name"
            )
        }
)
public class Group {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer groupId;

    @Column(name = "group_name")
    private String groupName;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "group_user",
            joinColumns = { @JoinColumn(name = "group_id") },
            inverseJoinColumns = { @JoinColumn(name = "id") }
    )
    private List<User> members;

    @JsonIgnoreProperties("group")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private List<Message> messages;
}
