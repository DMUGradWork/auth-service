package com.grewmeet.user.auth.repository;

import com.grewmeet.user.auth.domain.AuthCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthCredentialRepository extends JpaRepository<AuthCredential, Long> {
    boolean existsByEmail(String email);
}
