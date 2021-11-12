package com.meli.fede.markoo.access.controller;

import com.meli.fede.markoo.access.controller.model.AccessManagerValuesJson;
import com.meli.fede.markoo.access.manager.AccessManagerValues;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Order(-2)
@RestController("/accessControllerValues")
@RequiredArgsConstructor
public class AccessController {

    private final AccessManagerValues accessManagerValues;

    @PostMapping
    public void setAccessManagerValues(@RequestBody @Valid final AccessManagerValuesJson request) {
    }
}
