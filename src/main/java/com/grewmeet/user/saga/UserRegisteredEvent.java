package com.grewmeet.user.saga;

import com.grewmeet.user.auth.domain.AuthCredential;
import java.time.LocalDateTime;

public record UserRegisteredEvent(
        String email,
        String userId,
        LocalDateTime registeredAt
) {
    public static UserRegisteredEvent of(AuthCredential credential) {
        return new UserRegisteredEvent(
                credential.getEmail(),
                credential.getUserId(),
                credential.getCreatedAt()
        );
    }
}
