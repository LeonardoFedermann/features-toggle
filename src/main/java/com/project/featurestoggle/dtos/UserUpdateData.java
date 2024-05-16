package com.project.featurestoggle.dtos;

import com.project.featurestoggle.utils.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateData(
        @Size(
                min = Constants.NAME_MINIMUM_CHARACTERS,
                max = Constants.NAME_LIMIT_OF_CHARACTERS,
                message = "{name.size}"
        )
        String name,

        @Email(message = "{email.format}")
        String email,

        @Pattern(
                regexp = Constants.PASSWORD_FORMAT_REGEX,
                message = "{password.pattern}"
        )
        String password
) {
}
