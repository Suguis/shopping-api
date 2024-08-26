package com.shopping.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Shopping API", version = "0.1.0"))
public class ApiApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
