package com.project.featurestoggle.dtos;

public record UserCreateData(
        String email,
        String name,
        String password
) {
}
