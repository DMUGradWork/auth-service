package com.grewmeet.user.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grewmeet.user.auth.domain.User;

@JsonInclude(JsonInclude.Include.ALWAYS)
public record UserSearchResponse(
    Long id,
    String userId,
    String email,
    String name,
    String phoneNumber,
    String role,
    String gender,
    String region,
    String personality,
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
                user.getGender() != null ? user.getGender() : "",
                user.getRegion() != null ? user.getRegion() : "",
                user.getPersonality() != null ? user.getPersonality() : "",
                user.getCreatedAt().toString()
        );
    }
}
