package com.meli.fede.markoo.proxy.access.counter;

import com.meli.fede.markoo.proxy.data.mongo.model.RequestData;
import com.meli.fede.markoo.proxy.data.mongo.repository.MongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessCounter {
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
