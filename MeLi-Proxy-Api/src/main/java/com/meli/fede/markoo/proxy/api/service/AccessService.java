package com.meli.fede.markoo.proxy.api.service;

import com.meli.fede.markoo.proxy.api.data.repository.RedisRepository;
import com.meli.fede.markoo.proxy.api.values.AccessManagerValues;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessService {

    private final AccessManagerValues accessManagerValues;
    private final MetricsService metricsService;
    private final RedisRepository redisRepository;

    public boolean validateAccess(final String servletPath, final String host, final String userAgent) {
        final boolean access = this.validateIp(host)
                && this.validatePath(servletPath)
                && this.validateCombo(host, servletPath)
                && this.validateUserAgent(userAgent)
                && !this.isIpInBlacklist(host)
                && !this.isUserAgentInBlacklist(userAgent);
        this.metricsService.incrementMetrics(host, servletPath, userAgent, !access);
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
