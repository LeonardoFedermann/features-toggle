package com.project.featurestoggle.dtos;

import com.project.featurestoggle.domains.Feature;

import java.util.Date;

public record FeatureDetailData(Long id, String name, String description, Boolean isActive, Long createdBy,
                                Date createdWhen, Long updatedBy, Date updatedWhen) {
    public FeatureDetailData(Feature feature) {
        this(feature.getId(), feature.getName(), feature.getDescription(), feature.getIsActive(), feature.getCreatedBy(), feature.getCreatedWhen(), feature.getUpdatedBy(), feature.getUpdatedWhen());
    }
}
