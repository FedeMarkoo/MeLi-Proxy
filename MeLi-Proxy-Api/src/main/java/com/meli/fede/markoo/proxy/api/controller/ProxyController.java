package com.meli.fede.markoo.proxy.api.controller;

import com.meli.fede.markoo.proxy.api.service.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequiredArgsConstructor
public class ProxyController {

    private final AccessService accessService;

    @RequestMapping(path = "/**")
    public Object redirect(final HttpServletRequest request
            , final HttpServletResponse response
            , final HttpMethod httpMethod
            , @RequestHeader(value = "User-Agent", required = false) String userAgent
            , @RequestHeader(value = "Host", required = false) String host
            , @RequestBody(required = false) final Object body) {

        if (userAgent == null) {
            userAgent = "undefined";
        }
        if (host == null) {
            host = request.getRemoteHost();
        }
        final String path = request.getServletPath();
        if (this.accessService.validateAccess(path, host, userAgent)) {
            return this.accessService.processProxy(response, httpMethod, body, path);
        } else {
            response.setStatus(429);
            return null;
        }
    }

}
