package com.meli.fede.markoo.proxy.access.controller;

import com.meli.fede.markoo.proxy.access.controller.model.AccessManagerValuesJson;
import com.meli.fede.markoo.proxy.service.ProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccessController {

    private final ProxyService proxyService;

    @PostMapping("/accessControllerValues")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void accessControllerValues(@RequestBody @Valid final AccessManagerValuesJson request) {
        this.proxyService.setAccessManagerValues(request);
    }

    @GetMapping("/accessControllerValues")
    public ResponseEntity<AccessManagerValuesJson> accessControllerValues() {
        return ResponseEntity.ok(this.proxyService.getAccessManagerValues());
    }

    @RequestMapping("/accessControllerValues")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void accessControllerValuesNotImpl() {
    }
}
