package com.project.featurestoggle.dtos;

import com.project.featurestoggle.entities.User;

public record UserDetailData(
        Long id,
        String name,
        String email,
        Boolean isActive,
        Long createdBy,
        Long updatedBy
) {
    public UserDetailData(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getIsActive(),
                user.getCreatedBy(),
                user.getUpdatedBy()
        );
    }
}
