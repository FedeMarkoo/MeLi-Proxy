package com.meli.fede.markoo.proxy.manager.service;

import com.meli.fede.markoo.proxy.manager.data.model.RequestData;
import com.meli.fede.markoo.proxy.manager.data.repository.MongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {

    private final MongoRepository repository;

    private static Comparator<RequestData> getComparator() {
        return (rd1, rd2) -> Long.compare(rd2.getDeniedCant(), rd1.getDeniedCant());
    }

    private List<RequestData> getData() {
        return this.repository.getData();
    }

    public Map<String, Long> getIpsDenied() {
        return this.getData().stream()
                .filter(d -> d.getDeniedCant() > 0)
                .sorted(LogService.getComparator())
                .collect(Collectors.groupingBy(
                        RequestData::getIp
                        , LinkedHashMap::new
                        , Collectors.summingLong(RequestData::getDeniedCant)));
    }

    public Map<String, Long> getPathsDenied() {
        return this.getData().stream()
                .filter(d -> d.getDeniedCant() > 0)
                .sorted(LogService.getComparator())
                .collect(Collectors.groupingBy(
                        RequestData::getPath
                        , LinkedHashMap::new
                        , Collectors.summingLong(RequestData::getDeniedCant)));
    }
}
