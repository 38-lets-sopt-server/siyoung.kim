package org.sopt.global.jwt.repository;

import org.sopt.global.jwt.entity.AccessTokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccessTokenBlacklistRepository extends JpaRepository<AccessTokenBlacklist, Long> {
    boolean existsByAccessToken(String accessToken);

    // 나중에는 아래 메소드를 활용해서 black list 에 만료된 accessToken 들을 정리하는 스케줄러 달 수도 있음!
    // void deleteByExpiresAtBefore(LocalDateTime now);
}
