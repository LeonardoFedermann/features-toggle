package com.project.featurestoggle.dtos;

import com.project.featurestoggle.domains.User;

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
