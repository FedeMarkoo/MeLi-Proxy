package com.meli.fede.markoo.proxy.access.manager;

import com.meli.fede.markoo.proxy.access.counter.AccessCounter;
import com.meli.fede.markoo.proxy.data.redis.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessManager {

    private final AccessManagerValues accessManagerValues;
    private final AccessCounter accessCounter;
    private final RedisRepository redisRepository;

    public boolean validateAccess(final String servletPath, final String host, final String userAgent) {
        final boolean access = this.validateIp(host)
                && this.validatePath(servletPath)
                && this.validateCombo(host, servletPath)
                && this.validateUserAgent(userAgent)
                && !this.isIpInBlacklist(host)
                && !this.isUserAgentInBlacklist(userAgent);
        this.accessCounter.incrementMetrics(host, servletPath, userAgent, !access);
        return access;
    }

    private boolean isIpInBlacklist(final String host) {
        return this.redisRepository.isBlackIp(host);
    }

    private boolean isUserAgentInBlacklist(final String userAgent) {
        return this.redisRepository.isBlackUserAgent(userAgent);
    }

    private boolean validateUserAgent(final String userAgent) {
        final long value = this.redisRepository.getAndIncrement(userAgent);
        return value < this.accessManagerValues.getMaxRequestPerUserAgent();
    }

    private boolean validateCombo(final String ip, final String path) {
        final long value = this.redisRepository.getAndIncrement(ip.concat(path));
        return value < this.accessManagerValues.getMaxRequestPerCombo();
    }

    private boolean validatePath(final String path) {
        final long value = this.redisRepository.getAndIncrement(path);
        return value < this.accessManagerValues.getMaxRequestPerPath();
    }

    private boolean validateIp(final String ip) {
        final long value = this.redisRepository.getAndIncrement(ip);
        return value < this.accessManagerValues.getMaxRequestPerIp();
    }

}
