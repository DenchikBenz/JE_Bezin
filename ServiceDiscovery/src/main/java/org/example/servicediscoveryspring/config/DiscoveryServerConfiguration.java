package org.example.servicediscoveryspring.config;

import org.example.servicediscoveryspring.health.HealthCheckScheduler;
import org.example.servicediscoveryspring.registry.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebFlux
@EnableScheduling
public class DiscoveryServerConfiguration {

    @Bean
    public WebClient discoveryWebClient() {
        return WebClient.create();
    }

    @Bean
    public HealthCheckScheduler healthCheckScheduler(ServiceRegistry registry, WebClient webClient) {
        return new HealthCheckScheduler(registry, webClient);
    }


}
