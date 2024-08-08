package com.project.featurestoggle.dtos;

import com.project.featurestoggle.domains.Feature;

public record FeatureActivateAndDeactivateData(
        Long id,
        String name,
        Boolean isActive
) {
    public FeatureActivateAndDeactivateData(Feature feature) {
        this(feature.getId(), feature.getName(), feature.getIsActive());
    }
}
