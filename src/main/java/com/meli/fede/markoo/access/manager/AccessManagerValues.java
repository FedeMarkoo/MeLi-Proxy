package com.meli.fede.markoo.access.manager;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class AccessManagerValues {
    @NonNull
    @Value("${com.meli.fede.markoo.proxy.maxvalues.maxRequestPerIp}")
    public Integer maxRequestPerIp;

    @NonNull
    @Value("${com.meli.fede.markoo.proxy.maxvalues.maxRequestPerCombo}")
    public Integer maxRequestPerCombo;

    @NonNull
    @Value("${com.meli.fede.markoo.proxy.maxvalues.maxRequestPerPath}")
    public Integer maxRequestPerPath;
}
