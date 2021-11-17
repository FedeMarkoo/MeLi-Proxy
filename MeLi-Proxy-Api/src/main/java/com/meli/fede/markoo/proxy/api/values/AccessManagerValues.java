package com.meli.fede.markoo.proxy.api.values;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Document
@Component
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
