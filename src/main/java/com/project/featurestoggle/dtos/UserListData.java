package com.project.featurestoggle.dtos;

import com.project.featurestoggle.entities.User;
import lombok.AllArgsConstructor;

public record UserListData(
        Long id,
        String name,
        String email,
        Boolean isActive
) {
    public UserListData(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getIsActive());
    }
}
