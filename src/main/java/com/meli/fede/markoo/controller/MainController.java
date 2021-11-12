package com.meli.fede.markoo.controller;

import com.meli.fede.markoo.access.controller.model.AccessManagerValuesJson;
import com.meli.fede.markoo.proxy.service.ProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
public class MainController {

    private final ProxyService proxyService;
    @Value("${com.meli.fede.markoo.proxy.baseurl}")
    private String BASE_URL;

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

    @RequestMapping(value = "/**")
    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    public void redirect(final HttpServletRequest request, final HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Location", this.BASE_URL.concat(request.getRequestURI()));
    }
}
