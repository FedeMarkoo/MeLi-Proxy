package com.meli.fede.markoo.proxy.api.service;

import com.meli.fede.markoo.proxy.api.data.repository.RedisRepository;
import com.meli.fede.markoo.proxy.api.values.AccessManagerValues;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final AccessManagerValues accessManagerValues;
    private final MetricsService metricsService;
    private final RedisRepository redisRepository;
    @Value("${com.meli.fede.markoo.proxy.blacklist.factor}")
    private Integer factorAutoBlacklist;
    private WebClient localApiClient;

    @Autowired
    public void setLocalApiClient(@Value("${com.meli.fede.markoo.proxy.baseurl}") final String url) throws SSLException {
        final SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        final HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));
        this.localApiClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(url)
                .build();
    }


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
        return value <= this.accessManagerValues.getMaxRequestPerUserAgent();
    }

    private boolean validateCombo(final String ip, final String path) {
        final long value = this.redisRepository.getAndIncrement(ip.concat(path));
        return value <= this.accessManagerValues.getMaxRequestPerCombo();
    }

    private boolean validatePath(final String path) {
        final long value = this.redisRepository.getAndIncrement(path);
        return value <= this.accessManagerValues.getMaxRequestPerPath();
    }

    private boolean validateIp(final String ip) {
        final long value = this.redisRepository.getAndIncrement(ip);
        this.autoBlacklistIP(value, ip);
        return value <= this.accessManagerValues.getMaxRequestPerIp();
    }

    @Async
    public void autoBlacklistIP(final long value, final String host) {
        if (value > this.accessManagerValues.getMaxRequestPerIp() * this.factorAutoBlacklist) {
            this.redisRepository.blackIp(host);
        }
    }


    public Object processProxy(final HttpServletResponse response,
                               final HttpMethod httpMethod,
                               @RequestBody(required = false) final Optional<Object> body,
                               final String path) {
        final WebClient.RequestBodyUriSpec spec = this.localApiClient.method(httpMethod);
        body.ifPresent(spec::bodyValue);
        return spec.uri(path)
                .retrieve()
                .onStatus(HttpStatus::isError, cr -> {
                    response.setStatus(cr.rawStatusCode());
                    return Mono.empty();
                })
                .bodyToMono(Object.class)
                .block();
    }
}
