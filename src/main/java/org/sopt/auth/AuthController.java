package org.sopt.auth;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.auth.dto.LoginRequest;
import org.sopt.auth.dto.LoginResponse;
import org.sopt.global.common.code.SuccessCode;
import org.sopt.user.dto.response.UserResponse;
import org.sopt.global.jwt.TokenResponse;
import org.sopt.global.common.response.BaseResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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

    @Value("${security.jwt.refresh-token-expires-in-seconds:1209600}")
    private long refreshTokenExpiresInSeconds;

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
                .maxAge(Duration.ofSeconds(refreshTokenExpiresInSeconds))
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
        UserResponse user = authService.getUserById(userId);

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

        // 토큰 재발급 한 다음에
        TokenResponse tokens = authService.reissue(refreshToken);

        // 그걸 응답으로 내려주기
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.refreshToken())
                .httpOnly(true)
                // 배포환경일 때는 secure(true), sameSite("None") 으로 변경해야함
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofSeconds(refreshTokenExpiresInSeconds))
                .build();

        return BaseResponse.success(
                SuccessCode.SUCCESS_OK,
                LoginResponse.from(tokens),
                refreshTokenCookie
        );
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Void>> logout(
            Authentication authentication,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("인증되지 않았습니다.");
        }

        Long userId = Long.parseLong(authentication.getName());
        String accessToken = extractAccessToken(authorizationHeader);

        authService.logout(userId, accessToken);

        // 값 없는 refresh token 값 내려주기
        ResponseCookie deleteRefreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        return BaseResponse.success(
                SuccessCode.SUCCESS_OK,
                null,
                deleteRefreshTokenCookie
        );
    }

    /* 헤더로 온 accessToken 값은 authorizationHeader = "Bearer eyJhbGciOiJIUzI1NiJ9..."
    이런 식으로 오기 때문에 앞에 있는 Bearer 를 trim 하고 토큰값만 추출하는 헬퍼 메소드
     */
    private String extractAccessToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Access Token이 없습니다.");
        }

        return authorizationHeader.substring("Bearer ".length()).trim();
    }

}
