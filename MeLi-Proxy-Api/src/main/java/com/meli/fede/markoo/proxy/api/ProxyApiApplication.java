package com.meli.fede.markoo.proxy.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
        info = @Info(
                title = "MELI",
                version = "1.0.0"))
@EnableAsync
@EnableScheduling
@EnableRedisRepositories
@SpringBootApplication
public class ProxyApiApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ProxyApiApplication.class, args);
    }

}
