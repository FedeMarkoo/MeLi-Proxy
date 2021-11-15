package com.meli.fede.markoo.proxy.access.controller;

import com.meli.fede.markoo.proxy.access.service.BlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/blacklist")
@RequiredArgsConstructor
public class BlacklistController {

    private final BlacklistService service;

    @PutMapping("/ip/{host}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBlackIp(@PathVariable final String host) {
        this.service.setBlackHost(host);
    }

    @DeleteMapping("/ip/{host}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBlackIp(@PathVariable final String host) {
        this.service.setWhiteHost(host);
    }


    @GetMapping("/ip")
    public ResponseEntity<Set<String>> getAllBlackIp() {
        return ResponseEntity.ok(this.service.getAllBlackIp());
    }

    @GetMapping("/ip/{host}")
    public ResponseEntity<Boolean> isBlackIp(@PathVariable final String host) {
        return ResponseEntity.ok(this.service.isBlackHost(host));
    }

    @RequestMapping("/ip/*")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void ipNotImpl() {
    }

    @PutMapping("/userAgent/{userAgent}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBlackUserAgent(@PathVariable final String userAgent) {
        this.service.setBlackUserAgent(userAgent);
    }

    @DeleteMapping("/userAgent/{userAgent}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBlackUserAgent(@PathVariable final String userAgent) {
        this.service.setWhiteUserAgent(userAgent);
    }

    @GetMapping("/userAgent")
    public ResponseEntity<Set<String>> getAllBlackUserAgent() {
        return ResponseEntity.ok(this.service.getAllBlackUserAgent());
    }

    @GetMapping("/userAgent/{userAgent}")
    public ResponseEntity<Boolean> isBlackUserAgent(@PathVariable final String userAgent) {
        return ResponseEntity.ok(this.service.isBlackUserAgent(userAgent));
    }

    @RequestMapping("/userAgent/*")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void userAgentNotImpl() {
    }
}
