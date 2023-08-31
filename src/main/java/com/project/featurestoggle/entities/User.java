package com.project.featurestoggle.entities;

import com.project.featurestoggle.dtos.UserCreateData;
import jakarta.persistence.*;

@Table(name = "users")
@Entity(name = "user")
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
}
