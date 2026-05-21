package org.sopt.global.jwt.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private RefreshToken(Long userId, String token, LocalDateTime expiresAt) {
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public static RefreshToken of(Long userId, String token, long expiresInSeconds) {
        return new RefreshToken(
                userId,
                token,
                LocalDateTime.now().plusSeconds(expiresInSeconds)
        );
    }

    public void rotate(String newToken, long expiresInSeconds) {
        this.token = newToken;
        this.expiresAt = LocalDateTime.now().plusSeconds(expiresInSeconds);
    }
}