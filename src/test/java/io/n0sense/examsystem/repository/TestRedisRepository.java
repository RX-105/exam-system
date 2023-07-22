package io.n0sense.examsystem.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestRedisRepository {
    @Autowired
    RedisRepository redisRepository;

    @Test
    void testGet() {
        redisRepository.set("test", "value");
        System.out.println(redisRepository.get("test"));
    }
}
