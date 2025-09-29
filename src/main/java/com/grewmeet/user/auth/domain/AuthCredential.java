package com.grewmeet.user.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthCredential extends BaseEntity {

    @Column(nullable = false, unique = true, name = "user_id")
    private String userId;

    @Email
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @JsonIgnore
    @Column(nullable = false, name = "password_hash")
    private String passwordHash;

    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts = 0;

    @Column(name = "account_locked_until")
    private LocalDateTime accountLockedUntil;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuthStatus status;

    private AuthCredential(String userId, String email, String passwordHash) {
        this.userId = userId;
        this.email = email.toLowerCase().trim();
        this.passwordHash = passwordHash;
        this.passwordChangedAt = LocalDateTime.now();
        this.status = AuthStatus.PENDING;
    }

    private AuthCredential(String userId,
                           String email,
                           String passwordHash,
                           AuthStatus status) {
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.status = status;
    }

    public static AuthCredential create(String userId, String email, String passwordHash) {
        return new AuthCredential(userId, email, passwordHash);
    }

    public void confirm() {
        if(this.status != AuthStatus.PENDING) {
            throw new IllegalStateException("Cannot confirm auth credential");
        }
        this.status = AuthStatus.ACTIVE;
    }

}
