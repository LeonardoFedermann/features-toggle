package com.project.featurestoggle.domains;

import com.project.featurestoggle.dtos.UserCreateData;
import com.project.featurestoggle.dtos.UserUpdateData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Table(name = "users")
@Entity(name = "user")
@Getter
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

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

    public User(UserCreateData userCreateData) {
        this.name = userCreateData.name();
        this.password = userCreateData.password();
        this.email = userCreateData.email();
        this.isActive = true;
        this.createdBy = Long.valueOf(1);
        this.createdWhen = Date.from(Instant.now());
        this.updatedBy = Long.valueOf(1);
        this.updatedWhen = Date.from(Instant.now());
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

        this.updatedWhen = Date.from(Instant.now());

        //TODO: update "updatedBy" property with the ID of the logged user who sent the request
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
