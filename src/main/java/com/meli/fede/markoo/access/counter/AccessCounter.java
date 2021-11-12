package com.meli.fede.markoo.access.counter;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AccessCounter {

    private final Map<ObjectCounter, Integer> map = new ConcurrentHashMap<>();

    @Async
    public void incrementMetrics(final String ip, final String path) {
        final ObjectCounter objectCounter = new ObjectCounter(ip, path);
        final Integer cant = this.map.getOrDefault(objectCounter, 0);
        this.map.put(objectCounter, cant);
    }

}
