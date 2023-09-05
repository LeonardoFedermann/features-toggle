package com.project.featurestoggle.entities;

import com.project.featurestoggle.dtos.UserCreateData;
import com.project.featurestoggle.dtos.UserUpdateData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Table(name = "users")
@Entity(name = "user")
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    public User(UserCreateData userCreateData) {
        this.name = userCreateData.name();
        this.password = userCreateData.password();
        this.email = userCreateData.email();
        this.isActive = true;
        this.createdBy = Long.valueOf(1);
        this.updatedBy = Long.valueOf(1);
    }

    public void update(UserUpdateData userUpdateData) {
        if (!Objects.isNull(userUpdateData.name())) {
            this.name = userUpdateData.name();
        }

        if (!Objects.isNull(userUpdateData.email())) {
            this.email = userUpdateData.email();
        }

        if (!Objects.isNull(userUpdateData.password())) {
            this.password = userUpdateData.password();
        }

        //TODO: update "updatedBy" property with the ID of the logged user who sent the request
    }

    public void toggle() {
        this.isActive = !this.isActive;
    }
}
