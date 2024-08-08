package com.project.featurestoggle.controllers;

import com.project.featurestoggle.dtos.*;
import com.project.featurestoggle.services.FeaturesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/features")
public class FeaturesController {
    @Autowired
    private FeaturesService featuresService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Page<FeatureListData> list(@PageableDefault(size = 10, sort = {"name"}) Pageable pageable) {
        return featuresService.list(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FeatureDetailData detail(@PathVariable Long id) {
        return featuresService.detail(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public FeatureDetailData create(@RequestBody @Valid FeatureCreateData featureCreateData) {
        return featuresService.create(featureCreateData);
    }

    @PutMapping("/{id}")
    @Transactional
    @ResponseStatus(value = HttpStatus.OK)
    public FeatureDetailData update(
            @PathVariable Long id,
            @RequestBody @Valid FeatureUpdateData featureUpdateData
    ) {
        return featuresService.update(id, featureUpdateData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        featuresService.delete(id);
    }

    @PatchMapping("/activate/{id}")
    @Transactional
    @ResponseStatus(value = HttpStatus.OK)
    public FeatureActivateAndDeactivateData activate(@PathVariable Long id) {
        return featuresService.activate(id);
    }

    @PatchMapping("/deactivate/{id}")
    @Transactional
    @ResponseStatus(value = HttpStatus.OK)
    public FeatureActivateAndDeactivateData deactivate(@PathVariable Long id) {
        return featuresService.deactivate(id);
    }
}
