package com.grewmeet.user.auth.repository;

import com.grewmeet.user.auth.domain.User;
import com.grewmeet.user.auth.domain.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUserId(String userId);
    
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    
    boolean existsByUserId(String userId);
    
    Page<User> findByRole(UserRole role, Pageable pageable);
    
}
