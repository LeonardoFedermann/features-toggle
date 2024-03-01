package com.project.featurestoggle.data;

import com.project.featurestoggle.domains.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public Page<User> findAllByIsActiveTrue(Pageable pageable);
}
