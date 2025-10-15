package com.grewmeet.user.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grewmeet.user.auth.domain.AuthCredential;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RegisterResponse (
    String email,
    String userId
) {

    public static RegisterResponse from(AuthCredential credential) {
        return new RegisterResponse(
                credential.getEmail(),
                credential.getUserId());
    }

}
