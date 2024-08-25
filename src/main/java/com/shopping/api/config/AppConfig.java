package com.shopping.api.config;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${cart.deletion.time}")
    private Duration cartDeletionTime;

    @Bean
    public Duration cartDeletionTime() {
        return cartDeletionTime;
    }

    @Bean
    public ScheduledExecutorService cartDeletionExecutor() {
        return new ScheduledThreadPoolExecutor(1);
    }
}
