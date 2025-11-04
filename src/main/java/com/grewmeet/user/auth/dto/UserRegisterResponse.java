package com.grewmeet.user.auth.dto;

import com.grewmeet.user.auth.domain.User;

public record UserRegisterResponse(
    String email,
    String userId,
    String name,
    String phoneNumber,
    String role,
    String gender
) {
    public static UserRegisterResponse from(User user) {
        return new UserRegisterResponse(
                user.getEmail(),
                user.getUserId(),
                user.getName(),
                user.getPhoneNumber(),
                user.getRole().getDisplayName(),
                user.getGender()
        );
    }
}
