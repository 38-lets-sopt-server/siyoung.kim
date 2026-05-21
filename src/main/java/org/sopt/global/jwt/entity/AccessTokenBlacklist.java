package org.sopt.global.jwt.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessTokenBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accessToken;

    // userId를 foreign key 로 안잡는 이유는, 이 entity 는 인증용 테이블이기 때문이다! 값만 있으면 됨
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private AccessTokenBlacklist(Long userId, String accessToken, LocalDateTime expiresAt) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
    }

    public static AccessTokenBlacklist of(Long userId, String accessToken, LocalDateTime expiresAt) {
        return new AccessTokenBlacklist(userId, accessToken, expiresAt);
    }

}
