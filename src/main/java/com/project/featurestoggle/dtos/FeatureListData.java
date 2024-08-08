package com.project.featurestoggle.dtos;

import com.project.featurestoggle.domains.Feature;

public record FeatureListData(
        Long id,
        String name,
        String description,
        Boolean isActive
) {
    public FeatureListData(Feature feature) {
        this(feature.getId(), feature.getName(), feature.getDescription(), feature.getIsActive());
    }
}
