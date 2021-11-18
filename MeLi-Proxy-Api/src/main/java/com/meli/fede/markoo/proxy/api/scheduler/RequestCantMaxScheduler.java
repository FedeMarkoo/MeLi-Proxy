package com.meli.fede.markoo.proxy.api.scheduler;

import com.meli.fede.markoo.proxy.api.data.repository.MongoRepository;
import com.meli.fede.markoo.proxy.api.values.AccessManagerValues;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnMissingBean(type = "org.springframework.boot.test.mock.mockito.MockitoPostProcessor")
public class RequestCantMaxScheduler {

    private final MongoRepository repository;
    private final AccessManagerValues value;
    @Value("${com.meli.fede.markoo.proxy.defaultMax}")
    private Integer defaultMax;

    @Scheduled(initialDelay = 0, fixedDelayString = "${com.meli.fede.markoo.proxy.refresh-cant-delay}")
    public void maxRequestCant() {
        AccessManagerValues accessManagerValues = this.repository.getMaxCantRequest();
        if (accessManagerValues == null) {
            accessManagerValues = new AccessManagerValues();
            accessManagerValues.setMaxRequestPerIp(this.defaultMax);
            accessManagerValues.setMaxRequestPerPath(this.defaultMax);
            accessManagerValues.setMaxRequestPerCombo(this.defaultMax);
            accessManagerValues.setMaxRequestPerUserAgent(this.defaultMax);
        }
        final Integer maxRequestPerIp = accessManagerValues.getMaxRequestPerIp();
        final Integer maxRequestPerPath = accessManagerValues.getMaxRequestPerPath();
        final Integer maxRequestPerCombo = accessManagerValues.getMaxRequestPerCombo();
        final Integer maxRequestPerUserAgent = accessManagerValues.getMaxRequestPerUserAgent();

        this.value.setMaxRequestPerIp(this.getNormalizedValue(maxRequestPerIp, this.value.getMaxRequestPerIp()));
        this.value.setMaxRequestPerPath(this.getNormalizedValue(maxRequestPerPath, this.value.getMaxRequestPerPath()));
        this.value.setMaxRequestPerCombo(this.getNormalizedValue(maxRequestPerCombo, this.value.getMaxRequestPerCombo()));
        this.value.setMaxRequestPerUserAgent(this.getNormalizedValue(maxRequestPerUserAgent, this.value.getMaxRequestPerUserAgent()));
    }

    private int getNormalizedValue(final Integer value, Integer actual) {
        actual = actual == null ? this.defaultMax : actual;
        return value == null || value < 0 ? actual : value;
    }
}

