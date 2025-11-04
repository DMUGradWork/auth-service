package com.grewmeet.user.auth.repository;

import com.grewmeet.user.auth.domain.User;
import com.grewmeet.user.auth.domain.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUserId(String userId);
    
    // 이메일을 소문자로 변환하여 대소문자 구분 없이 검색
    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT COUNT(u) FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    long countByEmail(@Param("email") String email);
    
    default boolean existsByEmail(String email) {
        return countByEmail(email) > 0;
    }
    
    boolean existsByUserId(String userId);
    
    Page<User> findByRole(UserRole role, Pageable pageable);
    
}
