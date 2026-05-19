package org.sopt.auth;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.global.common.code.SuccessCode;
import org.sopt.user.dto.response.UserResponse;
import org.sopt.global.jwt.TokenResponse;
import org.sopt.global.common.response.BaseResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인 (Access Token + Refresh Token 발급)")
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(
            @RequestBody @Valid
            LoginRequest request
    ) {
        TokenResponse tokens = authService.login(request.email(), request.password());

        // tokens에서 refreshToken 만 분리해서 cookie 에다가 저장하기
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.refreshToken())
                .httpOnly(true)
                // 배포환경일때는 secure(true), sameSite("None") 으로 변경해야함
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(14))
                .build();

        //refresh token 까지 body 에 실어서 보내는데, 이걸 쿠키에 담아서 보내는 방식으로 바꿔야함
        // accessToken 은 ResponseBody 에, refreshToken 은 HttpOnly 쿠키에!
        return BaseResponse.success(
                SuccessCode.SUCCESS_OK,
                LoginResponse.from(tokens),
                refreshTokenCookie
        );
    }

    @Operation(summary = "내 정보 조회 (Access Token 검증)")
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<UserResponse>> me(Authentication authentication) {

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("인증되지 않았습니다.");
        }

        Long userId = Long.parseLong(authentication.getName());
        UserResponse user = authService.getMemberById(userId);

        return BaseResponse.success(
                SuccessCode.SUCCESS_OK,
                user);
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<BaseResponse<LoginResponse>> reissue(
            @CookieValue(name = "refreshToken", required = false)
            String refreshToken
    ) {
        TokenResponse tokens = authService.reissue(refreshToken);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.refreshToken())
                .httpOnly(true)
                // 배포환경일 때는 secure(true), sameSite("None") 으로 변경해야함
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(14))
                .build();

        return BaseResponse.success(
                SuccessCode.SUCCESS_OK,
                LoginResponse.from(tokens),
                refreshTokenCookie
        );
    }

}
