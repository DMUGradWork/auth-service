package com.grewmeet.user.auth.domain;

public enum UserRole {
    USER("사용자"),
    ADMIN("관리자");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
