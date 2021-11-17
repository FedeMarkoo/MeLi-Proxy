package com.meli.fede.markoo.proxy.manager.controller;

import com.meli.fede.markoo.proxy.manager.response.ComboInfoResponse;
import com.meli.fede.markoo.proxy.manager.response.IpInfoResponse;
import com.meli.fede.markoo.proxy.manager.response.PathInfoResponse;
import com.meli.fede.markoo.proxy.manager.response.UserAgentInfoResponse;
import com.meli.fede.markoo.proxy.manager.service.CounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/counter")
public class CounterController {

    private final CounterService counterService;

    @GetMapping("/ip")
    public ResponseEntity<List<IpInfoResponse>> counterIp() {
        return ResponseEntity.ok(this.counterService.getCounterByIp());
    }

    @GetMapping("/path")
    public ResponseEntity<List<PathInfoResponse>> counterPath() {
        return ResponseEntity.ok(this.counterService.getCounterByPath());
    }

    @GetMapping("/combo")
    public ResponseEntity<List<ComboInfoResponse>> counterCombo() {
        return ResponseEntity.ok(this.counterService.getCounterByCombo());
    }

    @GetMapping("/userAgent")
    public ResponseEntity<List<UserAgentInfoResponse>> counterUserAgent() {
        return ResponseEntity.ok(this.counterService.getCounterByUserAgent());
    }
}
