package com.project.featurestoggle.dtos;

import com.project.featurestoggle.entities.User;

public record UserToggleData(
        Long id,
        String name,
        Boolean isActive
) {
    public UserToggleData(User user) {
        this(user.getId(), user.getName(), user.getIsActive());
    }
}
