package com.project.featurestoggle.dtos;

import com.project.featurestoggle.utils.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FeatureCreateData(
        @NotBlank(message = "{name.mandatory}")
        @Size(
                min = Constants.FEATURE_NAME_MINIMUM_CHARACTERS,
                max = Constants.FEATURE_NAME_LIMIT_OF_CHARACTERS,
                message = "{name.size}"
        )
        String name,

        @Size(
                max = Constants.FEATURE_DESCRIPTION_LIMIT_OF_CHARACTERS,
                message = "{description.size}"
        )
        String description
) {
}
