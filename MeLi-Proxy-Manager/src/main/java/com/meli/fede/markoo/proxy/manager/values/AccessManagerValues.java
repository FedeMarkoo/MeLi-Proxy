package com.meli.fede.markoo.proxy.manager.values;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@EqualsAndHashCode
public class AccessManagerValues {
    @NonNull
    public Integer maxRequestPerIp;

    @NonNull
    public Integer maxRequestPerCombo;

    @NonNull
    public Integer maxRequestPerPath;

    @NonNull
    public Integer maxRequestPerUserAgent;
}
