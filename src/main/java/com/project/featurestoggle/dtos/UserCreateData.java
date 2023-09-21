package com.project.featurestoggle.dtos;

import com.project.featurestoggle.utils.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateData(
        @NotBlank(message = "{name.mandatory}")
        @Size(
                min = Constants.NAME_MINIMUM_CHARACTERS,
                max = Constants.NAME_LIMIT_OF_CHARACTERS,
                message = "{name.size}"
        )
        String name,

        @NotBlank(message = "{email.mandatory}")
        @Email(message = "{email.format}")
        @Size(
                max = Constants.EMAIL_LIMIT_OF_CHARACTERS,
                message = "{email.size}"
        )
        String email,

        @NotBlank(message = "{password.mandatory}")
        @Pattern(
                regexp = Constants.PASSWORD_FORMAT_REGEX,
                message = "{password.pattern}"
        )
        String password
) {
}
