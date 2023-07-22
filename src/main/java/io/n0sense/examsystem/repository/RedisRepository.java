package io.n0sense.examsystem.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RedisHash
@RequiredArgsConstructor
public class RedisRepository {
    private final RedisTemplate<String, String> template;

    public String get(String key) {
        return template.opsForValue().get(key);
    }

    public void set(String key, String value) {
        template.opsForValue().set(key, value);
    }

    public void set(String key, String value, Duration duration) {
        template.opsForValue().set(key, value, duration);
    }
}
