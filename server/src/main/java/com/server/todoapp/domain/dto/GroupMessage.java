package com.server.todoapp.domain.dto;

import com.server.todoapp.utils.DateUtils;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupMessage implements Comparable<GroupMessage> {
    private String message;
    private String username;
    private String dateTime;

    @Override
    public int compareTo(GroupMessage other) {
        LocalDateTime thisDateTime = DateUtils.parseDateTime(this.getDateTime());
        LocalDateTime otherDateTime = DateUtils.parseDateTime(other.getDateTime());

        if (thisDateTime.isAfter(otherDateTime)) {
            return 1;
        } else if (thisDateTime.isBefore(otherDateTime)) {
            return -1;
        }
        return 0;
    }
}
