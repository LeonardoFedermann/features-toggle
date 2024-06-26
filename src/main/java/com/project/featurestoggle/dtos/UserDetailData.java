package com.project.featurestoggle.dtos;

import com.project.featurestoggle.domains.User;

import java.util.Date;

public record UserDetailData(
        Long id,
        String name,
        String email,
        Boolean isActive,
        Long createdBy,
        Date createdWhen,
        Long updatedBy,
        Date updatedWhen
) {
    public UserDetailData(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getIsActive(),
                user.getCreatedBy(),
                user.getCreatedWhen(),
                user.getUpdatedBy(),
                user.getUpdatedWhen()
        );
    }
}
