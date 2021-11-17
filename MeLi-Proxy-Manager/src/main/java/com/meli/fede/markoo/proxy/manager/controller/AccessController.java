package com.meli.fede.markoo.proxy.manager.controller;

import com.meli.fede.markoo.proxy.manager.service.ProxyService;
import com.meli.fede.markoo.proxy.manager.values.AccessManagerValues;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/accessControllerValues")
@RequiredArgsConstructor
public class AccessController {

    private final ProxyService proxyService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void accessControllerValues(@RequestBody @Valid final AccessManagerValues request) {
        this.proxyService.setAccessManagerValues(request);
    }

    @GetMapping
    public ResponseEntity<AccessManagerValues> accessControllerValues() {
        return ResponseEntity.ok(this.proxyService.getAccessManagerValues());
    }

}
