package org.sopt.auth;

import lombok.RequiredArgsConstructor;
import org.sopt.global.common.code.ErrorCode;
import org.sopt.global.exception.BaseException;
import org.sopt.user.entity.User;
import org.sopt.user.dto.response.UserResponse;
import org.sopt.global.jwt.JwtService;
import org.sopt.global.jwt.RefreshToken;
import org.sopt.global.jwt.RefreshTokenRepository;
import org.sopt.global.jwt.TokenResponse;
import org.sopt.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${security.jwt.refresh-token-expires-in-seconds:1209600}")
    private long refreshTokenExpiresInSeconds;

    public UserResponse loginWithCredentials(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(ErrorCode.LOGIN_FAILED));

        if (!user.getPassword().equals(password)) {
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
    public UserResponse getMemberById(Long userId) {
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
}