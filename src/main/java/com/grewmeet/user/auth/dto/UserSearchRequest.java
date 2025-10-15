package com.grewmeet.user.auth.dto;

public record UserSearchRequest(
    String name,
    String email,
    String phoneNumber,
    int page,
    int size
) {}
