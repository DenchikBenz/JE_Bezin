package org.example.servicediscoveryspring.health;


import org.example.servicediscoveryspring.entity.ServiceInstance;
import org.example.servicediscoveryspring.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class HealthCheckScheduler {
    private static final Logger log = LoggerFactory.getLogger(HealthCheckScheduler.class);
    private final ServiceRegistry registry;
    private final RestTemplate rest = new RestTemplate();
    private final WebClient webClient;

    public HealthCheckScheduler(ServiceRegistry registry,WebClient webClient) {
        this.registry = registry;
        this.webClient = webClient;
    }

    @Scheduled(fixedDelay = 10_000)
    public void checkAll() {
        var snapshot = registry.snapshot();
        for (Map.Entry<String, List<ServiceInstance>> e : snapshot.entrySet()) {
            String service = e.getKey();
            for (ServiceInstance inst : e.getValue()) {
                String url = String.format("http://%s:%d/health", inst.host(), inst.port());
                try {
                    ResponseEntity<String> r = rest.getForEntity(url, String.class);
                    if (!r.getStatusCode().is2xxSuccessful()) {
                        registry.remove(service, inst);
                    }
                } catch (Exception ex) {
                    registry.remove(service, inst);
                }
            }
        }
    }
}