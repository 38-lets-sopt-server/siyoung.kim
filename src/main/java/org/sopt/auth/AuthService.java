package org.sopt.auth;

import lombok.RequiredArgsConstructor;
import org.sopt.global.common.code.ErrorCode;
import org.sopt.global.exception.BaseException;
import org.sopt.global.jwt.entity.AccessTokenBlacklist;
import org.sopt.global.jwt.repository.AccessTokenBlacklistRepository;
import org.sopt.user.entity.User;
import org.sopt.user.dto.response.UserResponse;
import org.sopt.global.jwt.JwtService;
import org.sopt.global.jwt.entity.RefreshToken;
import org.sopt.global.jwt.repository.RefreshTokenRepository;
import org.sopt.global.jwt.TokenResponse;
import org.sopt.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenBlacklistRepository accessTokenBlacklistRepository;
    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    @Value("${security.jwt.refresh-token-expires-in-seconds:1209600}")
    private long refreshTokenExpiresInSeconds;

    public UserResponse loginWithCredentials(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(ErrorCode.LOGIN_FAILED));

        // passwordEncoder 로 password 비교 방식으로 변경
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BaseException(ErrorCode.LOGIN_FAILED);
        }

        return UserResponse.from(user);
    }

    @Transactional
    public TokenResponse login(String email, String password) {
        UserResponse user = loginWithCredentials(email, password);

        String accessToken = jwtService.generateAccessToken(user.id(), user.email());
        String refreshToken = jwtService.generateRefreshToken(user.id());

        // 기존 Refresh Token 삭제 후 새로 저장
        refreshTokenRepository.deleteByUserId(user.id());
        refreshTokenRepository.save(
                RefreshToken.of(user.id(), refreshToken, refreshTokenExpiresInSeconds)
        );

        return TokenResponse.of(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return UserResponse.from(user);
    }

    @Transactional
    public TokenResponse reissue(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BaseException(ErrorCode.UNAUTHORIZED);
        }

        Long userId = jwtService.verifyAndGetMemberId(refreshToken);

        RefreshToken savedRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new BaseException(ErrorCode.UNAUTHORIZED));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());

        // accessToken 재발급시 refreshToken 도 다시 재발급하면 보안성 up
         String newRefreshToken = jwtService.generateRefreshToken(user.getId());

        savedRefreshToken.rotate(newRefreshToken, refreshTokenExpiresInSeconds);

        return TokenResponse.of(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(Long userId, String accessToken) {

        // refresh token 삭제
        refreshTokenRepository.deleteByUserId(userId);

        // accessToken 값이 blackList 에 없으면 black list 에 추가해준다
        if (!accessTokenBlacklistRepository.existsByAccessToken(accessToken)) {
            LocalDateTime expiresAt = jwtService.getExpiresAt(accessToken);

            accessTokenBlacklistRepository.save(
                    AccessTokenBlacklist.of(userId, accessToken, expiresAt)
            );
        }
    }
}