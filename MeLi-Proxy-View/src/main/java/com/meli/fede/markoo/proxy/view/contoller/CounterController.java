package com.meli.fede.markoo.proxy.view.contoller;

import com.meli.fede.markoo.proxy.view.response.ComboInfoResponse;
import com.meli.fede.markoo.proxy.view.response.IpInfoResponse;
import com.meli.fede.markoo.proxy.view.response.PathInfoResponse;
import com.meli.fede.markoo.proxy.view.response.UserAgentInfoResponse;
import com.meli.fede.markoo.proxy.view.service.CounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/counter")
public class CounterController {

    private final CounterService counterService;

    @GetMapping("/ip")
    public ResponseEntity<ArrayList<IpInfoResponse>> counterIp() {
        return ResponseEntity.ok(this.counterService.getCounterByIp());
    }

    @GetMapping("/path")
    public ResponseEntity<ArrayList<PathInfoResponse>> counterPath() {
        return ResponseEntity.ok(this.counterService.getCounterByPath());
    }

    @GetMapping("/combo")
    public ResponseEntity<ArrayList<ComboInfoResponse>> counterCombo() {
        return ResponseEntity.ok(this.counterService.getCounterByCombo());
    }

    @GetMapping("/userAgent")
    public ResponseEntity<ArrayList<UserAgentInfoResponse>> counterUserAgent() {
        return ResponseEntity.ok(this.counterService.getCounterByUserAgent());
    }
}
