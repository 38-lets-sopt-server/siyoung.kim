package org.sopt.auth.dto;

import org.sopt.global.jwt.TokenResponse;

public record LoginResponse(
        String accessToken
) {
    public static LoginResponse from(TokenResponse tokens) {
        return new LoginResponse(tokens.accessToken());
    }
}
