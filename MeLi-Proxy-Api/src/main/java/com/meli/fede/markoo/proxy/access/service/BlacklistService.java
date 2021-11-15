package com.meli.fede.markoo.proxy.access.service;

import com.meli.fede.markoo.proxy.data.redis.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class BlacklistService {

    private final RedisRepository redis;

    public boolean isBlackHost(final String host) {
        return this.redis.isBlackIp(host);
    }

    public void setBlackHost(final String host) {
        this.redis.blackIp(host, true);
    }

    public void setWhiteHost(final String host) {
        this.redis.blackIp(host, false);
    }

    public Set<String> getAllBlackUserAgent() {
        return this.redis.getAllBlackUserAgent();
    }

    public boolean isBlackUserAgent(final String host) {
        return this.redis.isBlackUserAgent(host);
    }

    public void setBlackUserAgent(final String host) {
        this.redis.blackUserAgent(host, true);
    }

    public void setWhiteUserAgent(final String host) {
        this.redis.blackUserAgent(host, false);
    }

    public Set<String> getAllBlackIp() {
        return this.redis.getAllBlackIp();
    }
}
