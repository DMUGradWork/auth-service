package com.grewmeet.user.auth.dto;

import com.grewmeet.user.auth.domain.User;

public record UserSearchResponse(
    Long id,
    String userId,
    String email,
    String name,
    String phoneNumber,
    String role,
    String createdAt
) {
    public static UserSearchResponse from(User user) {
        return new UserSearchResponse(
                user.getId(),
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getRole().getDisplayName(),
                user.getCreatedAt().toString()
        );
    }
}
