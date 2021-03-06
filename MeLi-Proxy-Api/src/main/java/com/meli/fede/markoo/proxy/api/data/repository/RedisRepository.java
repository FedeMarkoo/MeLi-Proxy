package com.meli.fede.markoo.proxy.api.data.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
    private static final String BLACK_LIST_IP = "blackListIp-";
    private static final String BLACK_LIST_USER_AGENT = "blackListUserAgent-";
    private final RedisTemplate<String, Integer> redisTemplate;
    @Value("${spring.cache.redis.time-to-live}")
    private long timeout;

    public Long getAndIncrement(final String key) {
        final ValueOperations<String, Integer> ops = this.redisTemplate.opsForValue();
        final Long increment = ops.increment(key, 1);
        this.redisTemplate.expire(key, this.timeout, TimeUnit.SECONDS);
        return increment;
    }

    public boolean isBlackIp(final String host) {
        return !Objects.requireNonNull(this.redisTemplate.keys(BLACK_LIST_IP.concat(host))).isEmpty();
    }

    public boolean isBlackUserAgent(final String userAgent) {
        return !Objects.requireNonNull(this.redisTemplate.keys(BLACK_LIST_USER_AGENT.concat(userAgent))).isEmpty();
    }

    public void blackIp(final String host) {
        this.redisTemplate.opsForValue().set(BLACK_LIST_IP.concat(host), 1);
    }
}
