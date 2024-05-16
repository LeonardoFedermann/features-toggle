package com.project.featurestoggle.dtos;

import com.project.featurestoggle.domains.User;

public record UserActivateAndDeactivatieData(
        Long id,
        String name,
        Boolean isActive
) {
    public UserActivateAndDeactivatieData(User user) {
        this(user.getId(), user.getName(), user.getIsActive());
    }
}
