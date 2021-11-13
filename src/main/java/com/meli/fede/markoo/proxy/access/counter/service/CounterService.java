package com.meli.fede.markoo.proxy.access.counter.service;

import com.meli.fede.markoo.proxy.access.counter.AccessCounter;
import com.meli.fede.markoo.proxy.access.counter.ObjectCounter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CounterService {

    private final AccessCounter accessCounter;

    public Map<String, Integer> getCounterByIp() {
        return this.accessCounter.getMap().entrySet()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                e -> e.getKey().getIp(), Collectors.summingInt(e -> e.getValue())
                        ));
    }

    public Map<String, Integer> getCounterByPath() {
        return this.accessCounter.getMap().entrySet()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                e -> e.getKey().getPath(), Collectors.summingInt(e -> e.getValue())
                        ));
    }

    public Map<String, Integer> getCounterByCombo() {
        return this.accessCounter.getMap().entrySet()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                e -> {
                                    final ObjectCounter key = e.getKey();
                                    return key.getIp().concat(" - ").concat(key.getPath());
                                }, Collectors.summingInt(e -> e.getValue())
                        ));
    }
}
