package io.n0sense.examsystem.dto;

import io.n0sense.examsystem.entity.User;
import lombok.Getter;

@Getter
public class BasicUserDTO {
    private Long userId;
    private String name;

    public BasicUserDTO(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
    }
}
