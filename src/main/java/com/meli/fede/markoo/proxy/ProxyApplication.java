package com.meli.fede.markoo.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProxyApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ProxyApplication.class, args);
    }

}
