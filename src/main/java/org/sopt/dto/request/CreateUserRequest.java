package org.sopt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @Schema(description = "사용자 닉네임", example = "히히롱")
        @NotBlank(message = "닉네임은 필수입니다.") // @NotBlank는 null 뿐만 아니라 공백 문자도 막아준다
        String nickname,

        @Schema(description = "사용자 이메일", example = "test@test.com")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        String email

) {

}