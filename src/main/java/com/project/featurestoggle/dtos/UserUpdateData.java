package com.project.featurestoggle.dtos;

import com.project.featurestoggle.utils.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateData(
        @Size(min = Constants.NAME_MINIMUM_CHARACTERS, max = Constants.NAME_LIMIT_OF_CHARACTERS)
        String name,

        @Email
        @Size(max = Constants.EMAIL_LIMIT_OF_CHARACTERS)
        String email,

        @Pattern(regexp = Constants.PASSWORD_FORMAT_REGEX)
        String password
) {
}
