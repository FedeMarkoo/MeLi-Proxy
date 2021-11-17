package com.meli.fede.markoo.proxy.api.scheduler;

import com.meli.fede.markoo.proxy.api.data.repository.MongoRepository;
import com.meli.fede.markoo.proxy.api.values.AccessManagerValues;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestCantMaxScheduler {

    private final MongoRepository repository;
    private final AccessManagerValues value;

    @Scheduled(initialDelay = 0, fixedDelayString = "${com.meli.fede.markoo.proxy.refresh-cant-delay}")
    public void maxRequestCant() {
        final AccessManagerValues accessManagerValues = this.repository.getMaxCantRequest();
        this.value.setMaxRequestPerIp(accessManagerValues.getMaxRequestPerIp());
        this.value.setMaxRequestPerPath(accessManagerValues.getMaxRequestPerPath());
        this.value.setMaxRequestPerCombo(accessManagerValues.getMaxRequestPerCombo());
        this.value.setMaxRequestPerUserAgent(accessManagerValues.getMaxRequestPerUserAgent());
    }
}
