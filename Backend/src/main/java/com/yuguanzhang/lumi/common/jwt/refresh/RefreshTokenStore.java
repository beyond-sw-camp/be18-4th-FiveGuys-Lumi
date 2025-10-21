package com.yuguanzhang.lumi.common.jwt.refresh;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RefreshTokenStore {
    private final StringRedisTemplate redis;
    // StringRedisTemplate Spring에서 제공하는 Redis 접근용 클래스

    private String key(String email) {
        return "Refresh:" + email;
    }

    public void save(String email, String refreshToken, long ttlMillis) {
        redis.opsForValue().set(key(email), refreshToken, Duration.ofMillis(ttlMillis));
    }

    public String find(String email) {
        return redis.opsForValue().get(key(email));
    }

    public void delete(String email) {
        redis.delete(key(email));
    }
}
