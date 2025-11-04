package com.grewmeet.user.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_user_id", columnList = "user_id", unique = true),
        @Index(name = "idx_users_email", columnList = "email", unique = true),
        @Index(name = "idx_users_role", columnList = "role")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, name = "user_id")
    private String userId;

    @Email
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String name;

    @Pattern(regexp = "^01[0-9]-[0-9]{4}-[0-9]{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
    @Column(nullable = false, length = 13)
    private String phoneNumber;

    @Column(length = 10)
    private String gender;

    @Column(length = 100)
    private String region;

    @Column(length = 200)
    private String personality; // 쉼표로 구분된 성격 태그들 (예: "열정,성실,도전")

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "auth_credential_id")
    private AuthCredential authCredential;

    private User(String userId, String email, UserRole role, String name, String phoneNumber, String gender, String region, String personality, AuthCredential authCredential) {
        this.userId = userId;
        this.email = email.toLowerCase().trim();
        this.role = role;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.region = region;
        this.personality = personality;
        this.authCredential = authCredential;
    }

    public static User createUser(String userId, String email, String name, String phoneNumber, String gender, String region, String personality, AuthCredential authCredential) {
        return new User(userId, email, UserRole.USER, name, phoneNumber, gender, region, personality, authCredential);
    }

    public static User createAdmin(String userId, String email, String phoneNumber, AuthCredential authCredential) {
        return new User(userId, email, UserRole.ADMIN, null, phoneNumber, null, null, null, authCredential);
    }

    public void updatePassword(String newPasswordHash) {
        this.authCredential.updatePassword(newPasswordHash);
    }

    public void deleteAuthCredential() {
        this.authCredential = null;
    }
}
