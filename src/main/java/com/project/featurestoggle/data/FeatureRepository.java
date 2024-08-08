package com.project.featurestoggle.data;

import com.project.featurestoggle.domains.Feature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {
    public Page<Feature> findAllByIsActiveTrue(Pageable pageable);
}
