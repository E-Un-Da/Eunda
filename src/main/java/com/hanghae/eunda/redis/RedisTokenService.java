package com.hanghae.eunda.redis;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTokenService {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisTokenService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String generateAndSaveToken() {
        String token = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(token, "valid");
        stringRedisTemplate.expire(token, 30, TimeUnit.MINUTES); // 예시: 30분 후에 토큰 만료
        return token;
    }

    public boolean isTokenValid(String token) {
        String value = stringRedisTemplate.opsForValue().get(token);
        return value != null && value.equals("valid");
    }

    public void deleteToken(String token) {
        stringRedisTemplate.delete(token);
    }
}