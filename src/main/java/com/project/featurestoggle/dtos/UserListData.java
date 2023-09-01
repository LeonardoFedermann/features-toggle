package com.project.featurestoggle.dtos;

import com.project.featurestoggle.entities.User;

public record UserListData(
        String name,
        String email,
        Boolean isActive
) {
    public UserListData(User user) {
        this(user.getName(), user.getEmail(), user.getIsActive());
    }
}