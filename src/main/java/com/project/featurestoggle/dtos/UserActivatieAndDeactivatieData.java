package com.project.featurestoggle.dtos;

import com.project.featurestoggle.domains.User;

public record UserActivatieAndDeactivatieData(
        Long id,
        String name,
        Boolean isActive
) {
    public UserActivatieAndDeactivatieData(User user) {
        this(user.getId(), user.getName(), user.getIsActive());
    }
}
