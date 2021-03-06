package com.meli.fede.markoo.proxy.manager.data.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
    private static final String BLACK_LIST_IP = "blackListIp-";
    private static final String BLACK_LIST_USER_AGENT = "blackListUserAgent-";
    private final RedisTemplate<String, Integer> redisTemplate;

    public boolean isBlackIp(final String host) {
        return !Objects.requireNonNull(this.redisTemplate.keys(BLACK_LIST_IP.concat(host))).isEmpty();
    }

    public boolean isBlackUserAgent(final String userAgent) {
        return !Objects.requireNonNull(this.redisTemplate.keys(BLACK_LIST_USER_AGENT.concat(userAgent))).isEmpty();
    }

    public void blackIp(final String host, final boolean isBlack) {
        if (isBlack) {
            this.redisTemplate.opsForValue().set(BLACK_LIST_IP.concat(host), 1);
        } else {
            this.redisTemplate.delete(BLACK_LIST_IP.concat(host));
        }
    }

    public void blackUserAgent(final String userAgent, final boolean isBlack) {
        if (isBlack) {
            this.redisTemplate.opsForValue().set(BLACK_LIST_USER_AGENT.concat(userAgent), 1);
        } else {
            this.redisTemplate.delete(BLACK_LIST_USER_AGENT.concat(userAgent));
        }
    }

    public Set<String> getAllBlackIp() {
        return this.getAllBlack(BLACK_LIST_IP);
    }

    public Set<String> getAllBlackUserAgent() {
        return this.getAllBlack(BLACK_LIST_USER_AGENT);
    }

    private Set<String> getAllBlack(final String pattern) {
        RedisConnection redisConnection = null;
        try {
            final RedisConnectionFactory connectionFactory = this.redisTemplate.getConnectionFactory();
            assert connectionFactory != null;
            redisConnection = connectionFactory.getConnection();

            final Set<byte[]> keys = redisConnection.keyCommands().keys(("*" + pattern + "*").getBytes());
            assert keys != null;
            return keys.stream()
                    .map(s -> new String(s).substring(pattern.length() + 7))
                    .collect(Collectors.toSet());
        } finally {
            if (redisConnection != null) {
                redisConnection.close(); //Ensure closing this connection.
            }
        }
    }
}
