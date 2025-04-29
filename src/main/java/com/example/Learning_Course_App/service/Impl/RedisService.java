package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.aop.ApiException;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class RedisService {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;


    public RedisService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void save(String key, String value, long expirationMinutes) {
        redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(expirationMinutes));
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    public <T> void saveList(String key, List<T> list, long expirationMinutes) {
        try {
            String json = objectMapper.writeValueAsString(list);
            save(key, json, expirationMinutes);
        } catch (JsonProcessingException e) {
            throw new ApiException(ErrorCode.REDIS_ERROR);
        }
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        try {
            String json = get(key);
            if (json == null) return null;
            return objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));

        } catch (JsonProcessingException e) {
            throw new ApiException(ErrorCode.REDIS_ERROR);
        }
    }
    public <T> void save(String key, T value, long expirationMinutes) {
        try {
            String json = objectMapper.writeValueAsString(value);
            save(key, json, expirationMinutes);
        } catch (JsonProcessingException e) {
            throw new ApiException(ErrorCode.REDIS_ERROR);
        }
    }
    public <T> T get(String key, Class<T> clazz) {
        try {
            String json = get(key);
            if (json == null) return null;
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new ApiException(ErrorCode.REDIS_ERROR);
        }
    }
}
