package com.hanghae.eunda.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenService {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisTokenService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String generateAndSaveToken(String email) {
        byte[] encodedBytes = Base64.getEncoder().encode(email.getBytes(StandardCharsets.UTF_8));
        String token = new String(encodedBytes, StandardCharsets.UTF_8);
        stringRedisTemplate.expire(token, 30, TimeUnit.MINUTES); // 예시: 30분 후에 토큰 만료
        return token;
    }

    public String isTokenValid(String token) {
        byte[] decodedBytes = Base64.getDecoder().decode(token.getBytes(StandardCharsets.UTF_8));
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    public void deleteToken(String token) {
        stringRedisTemplate.delete(token);
    }
}
