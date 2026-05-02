package org.sopt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateUserRequest(
        @Schema(description = "사용자 닉네임", example = "히히롱")
        String nickname,

        @Schema(description = "사용자 이메일", example = "test@test.com")
        String email

) {

}