package com.meli.fede.markoo.proxy.view.contoller;

import com.meli.fede.markoo.proxy.view.service.CounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CounterController {

    private final CounterService counterService;

    @GetMapping("/counter/ip")
    public ResponseEntity<Map<String, Integer>> counterIp() {
        return ResponseEntity.ok(this.counterService.getCounterByIp());
    }

    @GetMapping("/counter/path")
    public ResponseEntity<Map<String, Integer>> counterPath() {
        return ResponseEntity.ok(this.counterService.getCounterByPath());
    }

    @GetMapping("/counter/combo")
    public ResponseEntity<Map<String, Integer>> counterCombo() {
        return ResponseEntity.ok(this.counterService.getCounterByCombo());
    }
}
