package com.meli.fede.markoo.proxy.service;

import com.meli.fede.markoo.access.controller.model.AccessManagerValuesJson;
import com.meli.fede.markoo.access.manager.AccessManagerValues;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class ProxyService {
    private final AccessManagerValues accessManagerValues;

    public AccessManagerValuesJson getAccessManagerValues() {
        final AccessManagerValuesJson response = new AccessManagerValuesJson();
        response.setMaxRequestPerIp(this.accessManagerValues.getMaxRequestPerIp());
        response.setMaxRequestPerPath(this.accessManagerValues.getMaxRequestPerPath());
        response.setMaxRequestPerCombo(this.accessManagerValues.getMaxRequestPerCombo());
        return response;
    }

    public void setAccessManagerValues(@RequestBody @Valid final AccessManagerValuesJson request) {
        this.accessManagerValues.setMaxRequestPerIp(request.getMaxRequestPerIp());
        this.accessManagerValues.setMaxRequestPerPath(request.getMaxRequestPerPath());
        this.accessManagerValues.setMaxRequestPerCombo(request.getMaxRequestPerCombo());
    }
}
