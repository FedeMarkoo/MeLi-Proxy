package com.meli.fede.markoo.proxy.manager.controller;


import com.meli.fede.markoo.proxy.manager.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
public class LogController {

    private final LogService logService;

    @GetMapping("/ip/denied")
    public ResponseEntity<Map<String, Long>> getIpsDenied() {
        return ResponseEntity.ok(this.logService.getIpsDenied());
    }

    @GetMapping("/path/denied")
    public ResponseEntity<Map<String, Long>> getPathsDenied() {
        return ResponseEntity.ok(this.logService.getPathsDenied());
    }
}
