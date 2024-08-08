package com.project.featurestoggle.dtos;

import com.project.featurestoggle.utils.Constants;
import jakarta.validation.constraints.Size;

public record FeatureUpdateData(
        @Size(
                min = Constants.NAME_MINIMUM_CHARACTERS,
                max = Constants.NAME_LIMIT_OF_CHARACTERS,
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
