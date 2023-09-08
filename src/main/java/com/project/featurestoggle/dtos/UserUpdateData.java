package com.project.featurestoggle.dtos;

import jakarta.validation.constraints.NotNull;

public record UserUpdateData(
        String name,
        String email,
        String password
) {
}
