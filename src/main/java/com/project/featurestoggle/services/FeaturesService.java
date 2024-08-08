package com.project.featurestoggle.services;

import com.project.featurestoggle.data.FeatureRepository;
import com.project.featurestoggle.domains.Feature;
import com.project.featurestoggle.domains.User;
import com.project.featurestoggle.dtos.*;
import com.project.featurestoggle.exceptions.NotFoundException;
import com.project.featurestoggle.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FeaturesService {
    @Autowired
    private FeatureRepository featureRepository;

    public FeatureDetailData create(FeatureCreateData featureCreateData) {
        Feature feature = new Feature(featureCreateData);
        featureRepository.save(feature);

        return new FeatureDetailData(feature);
    }

    public Page<FeatureListData> list(Pageable pageable) {
        return featureRepository.findAllByIsActiveTrue(pageable).map(FeatureListData::new);
    }

    public FeatureDetailData detail(Long id) {
        Feature feature = featureRepository.findById(id).orElseThrow(
                () -> new NotFoundException(Constants.FEATURE_NOT_FOUND_MESSAGE)
        );
        return new FeatureDetailData(feature);
    }

    public FeatureDetailData update(Long id, FeatureUpdateData featureUpdateData) {
        Feature feature = featureRepository.findById(id).orElseThrow(
                () -> new NotFoundException(Constants.FEATURE_NOT_FOUND_MESSAGE)
        );
        // TODO: update "updatedBy" property with the ID of the logged user who sent the request
        // TODO: allow feature update only if the current logged user is the same one or if it is a privileged one.
        feature.update(featureUpdateData);
        return new FeatureDetailData(feature);
    }

    public void delete(Long id) {
        featureRepository.findById(id).orElseThrow(
                () -> new NotFoundException(Constants.FEATURE_NOT_FOUND_MESSAGE)
        );
        featureRepository.deleteById(id);
    }

    public FeatureActivateAndDeactivateData activate(Long id) {
        Feature feature = featureRepository.findById(id).orElseThrow(
                () -> new NotFoundException(Constants.USER_NOT_FOUND_MESSAGE)
        );
        feature.activate();
        return new FeatureActivateAndDeactivateData(feature);
    }

    public FeatureActivateAndDeactivateData deactivate(Long id) {
        Feature feature = featureRepository.findById(id).orElseThrow(
                () -> new NotFoundException(Constants.USER_NOT_FOUND_MESSAGE)
        );
        feature.deactivate();
        return new FeatureActivateAndDeactivateData(feature);
    }
}
