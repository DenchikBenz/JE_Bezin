package org.example.servicediscoveryspring;

import org.example.starter.properties.DiscoveryProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(DiscoveryProperties.class)
public class ServiceDIscoverySpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceDIscoverySpringApplication.class, args);
    }

}
