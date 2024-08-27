package com.shopping.api.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${cart.deletion.time}")
    private Duration cartDeletionTime;

    public Duration getCartDeletionTime() {
        return cartDeletionTime;
    }
}
