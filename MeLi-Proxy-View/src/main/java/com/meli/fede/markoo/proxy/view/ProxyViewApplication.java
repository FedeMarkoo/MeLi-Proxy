package com.meli.fede.markoo.proxy.view;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@OpenAPIDefinition(
        info = @Info(
                title = "MELI",
                version = "1.0.0"))
@SpringBootApplication
public class ProxyViewApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ProxyViewApplication.class, args);
    }

}
