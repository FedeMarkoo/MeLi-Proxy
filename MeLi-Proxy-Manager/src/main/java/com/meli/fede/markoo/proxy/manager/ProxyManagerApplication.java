package com.meli.fede.markoo.proxy.manager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;


@OpenAPIDefinition(
        info = @Info(
                title = "MELI",
                version = "1.0.0"))
@EnableAsync
@EnableRedisRepositories
@SpringBootApplication
public class ProxyManagerApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ProxyManagerApplication.class, args);
    }

}
