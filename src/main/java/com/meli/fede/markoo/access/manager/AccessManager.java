package com.meli.fede.markoo.access.manager;

import com.meli.fede.markoo.access.counter.AccessCounter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class AccessManager {
    private static final BiFunction<String, Integer, Integer> SUBTRACT = (k2, v) -> --v > 0 ? v : null;

    private final Map<String, Integer> ipMap = new ConcurrentHashMap<>();
    private final Map<String, Integer> pathMap = new ConcurrentHashMap<>();
    private final Map<String, Integer> comboMap = new ConcurrentHashMap<>();

    private final AccessManagerValues accessManagerValues;
    private final AccessCounter accessCounter;

    @Scheduled(cron = "${com.meli.fede.markoo.subtract.cron}")
    private void scheduleTaskUsingCronExpression() {
        this.ipMap.keySet().forEach(k -> this.ipMap.compute(k, SUBTRACT));
        this.pathMap.keySet().forEach(k -> this.pathMap.compute(k, SUBTRACT));
        this.comboMap.keySet().forEach(k -> this.comboMap.compute(k, SUBTRACT));
    }

    public boolean validateAccess(final HttpServletRequest request) {
        final String path = request.getServletPath();
        if ("/accessControllerValues".equals(path)) {
            return true;
        }
        final String ip = request.getRemoteHost();
        final boolean access = this.validateIp(ip) && this.validatePath(path) && this.validateCombo(ip, path);
        if (access) {
            this.accessCounter.incrementMetrics(ip, path);
        }
        return access;
    }

    private boolean validateCombo(final String ip, final String path) {
        final int value = this.comboMap.getOrDefault(path, 0) + 1;
        this.comboMap.put(ip.concat(path), value);
        return value < this.accessManagerValues.maxRequestPerCombo;
    }

    private boolean validatePath(final String path) {
        final int value = this.pathMap.getOrDefault(path, 0) + 1;
        this.pathMap.put(path, value);
        return value < this.accessManagerValues.maxRequestPerPath;
    }

    private boolean validateIp(final String ip) {
        final int value = this.ipMap.getOrDefault(ip, 0) + 1;
        this.ipMap.put(ip, value);
        return value < this.accessManagerValues.maxRequestPerIp;
    }
}
