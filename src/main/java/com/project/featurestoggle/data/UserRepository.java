package com.project.featurestoggle.data;

import com.project.featurestoggle.domains.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Page<User> findAllByIsActiveTrue(Pageable pageable);
    public User findByEmail(String email);
}
