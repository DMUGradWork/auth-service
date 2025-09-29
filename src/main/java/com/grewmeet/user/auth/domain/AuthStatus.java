package com.grewmeet.user.auth.domain;

public enum AuthStatus {
    PENDING("확인중"),
    ACTIVE("활성화"),
    LOCKED("잠김"),
    DISABLED("비활성화");

    private final String displayName;

    AuthStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
