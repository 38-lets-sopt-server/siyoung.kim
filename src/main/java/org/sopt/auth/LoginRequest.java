package org.sopt.auth;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest (
        @NotBlank @Email
        @Schema(description = "이메일", example = "hellot@hello.com")
        String email,
        @NotBlank
        @Schema(description = "비밀번호", example = "1234")
        String password
){
}
