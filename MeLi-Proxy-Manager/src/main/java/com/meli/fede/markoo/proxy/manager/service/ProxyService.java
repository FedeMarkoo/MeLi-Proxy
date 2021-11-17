package com.meli.fede.markoo.proxy.manager.service;

import com.meli.fede.markoo.proxy.manager.data.repository.MongoRepository;
import com.meli.fede.markoo.proxy.manager.values.AccessManagerValues;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class ProxyService {
    private final MongoRepository repository;

    public AccessManagerValues getAccessManagerValues() {
        final AccessManagerValues maxCantRequest = this.repository.getMaxCantRequest();
        return maxCantRequest;
    }

    public void setAccessManagerValues(@RequestBody @Valid final AccessManagerValues request) {
        this.repository.saveMaxCantRequest(request);
    }
}
