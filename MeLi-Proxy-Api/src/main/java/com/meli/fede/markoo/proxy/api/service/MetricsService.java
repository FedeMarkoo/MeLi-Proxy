package com.meli.fede.markoo.proxy.api.service;


import com.meli.fede.markoo.proxy.api.data.model.RequestData;
import com.meli.fede.markoo.proxy.api.data.repository.MongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MetricsService {
    private final MongoRepository repository;

    @SneakyThrows
    @Async
    public void incrementMetrics(final String ip, final String path, final String userAgent, final boolean denieded) {
        final RequestData data = RequestData.builder()
                .ip(ip)
                .path(path)
                .userAgent(userAgent)
                .build();
        this.repository.incCounter(data, denieded);
    }

}
