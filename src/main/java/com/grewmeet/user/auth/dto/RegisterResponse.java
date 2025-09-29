package com.grewmeet.user.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grewmeet.user.auth.domain.AuthCredential;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "A domain object for user detail information")
public record RegisterResponse (
    @Schema(title = "사용자 Email", description = "사용자 ID로 사용되는 Email 정보로써 로그인 시 사용")
    String email,

    @Schema(title = "사용자 User ID", description = "사용자 회원 가입 시 자동으로 부여 되는 사용자 고유한 ID (UUID로 Random하게 생성)")
    String userId
) {

    public static RegisterResponse from(AuthCredential credential) {
        return new RegisterResponse(
                credential.getEmail(),
                credential.getUserId());
    }

}
