package com.server.todoapp.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Comparable<Message> {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer msgId;

    @Column
    private String message;

    @Column
    private String username;

    @Column
    private LocalDateTime dateTime;

    @JsonIgnoreProperties("messages")
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "groupId")
    private Group group;

    @Override
    public int compareTo(Message other) {
        if (this.getDateTime().isAfter(other.getDateTime())) {
            return 1;
        } else if (this.getDateTime().isBefore(other.getDateTime())) {
            return -1;
        }
        return 0;
    }
}
