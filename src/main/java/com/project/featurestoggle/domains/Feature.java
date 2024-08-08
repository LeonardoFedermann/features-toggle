package com.project.featurestoggle.domains;

import com.project.featurestoggle.dtos.FeatureCreateData;
import com.project.featurestoggle.dtos.FeatureUpdateData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Table(name = "features")
@Entity(name = "feature")
@Getter
@NoArgsConstructor
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

    @CreatedDate
    @Column(name = "created_when")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdWhen;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;

    @LastModifiedDate
    @Column(name = "updated_when")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedWhen;

    public Feature(FeatureCreateData featureCreateData) {
        this.name = featureCreateData.name();
        this.description = featureCreateData.description();
        this.isActive = true;
        this.createdBy = Long.valueOf(1);
        this.createdWhen = Date.from(Instant.now());
        this.updatedBy = Long.valueOf(1);
        this.updatedWhen = Date.from(Instant.now());
    }

    public void update(FeatureUpdateData featureUpdateData) {
        if (!Objects.isNull(featureUpdateData.name())) {
            this.name = featureUpdateData.name();
        }

        if (!Objects.isNull(featureUpdateData.description())) {
            this.description = featureUpdateData.description();
        }

        this.updatedWhen = Date.from(Instant.now());

        //TODO: update "updatedBy" property with the ID of the logged user who sent the request
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }
}
