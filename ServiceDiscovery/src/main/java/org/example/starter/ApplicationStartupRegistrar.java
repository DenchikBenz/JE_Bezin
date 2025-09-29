package org.example.starter;

import org.example.starter.properties.DiscoveryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class ApplicationStartupRegistrar implements
        ApplicationListener<ApplicationReadyEvent> {

    private final DiscoveryProperties props;
    private final RestTemplate rest;
    private static final Logger log = LoggerFactory.getLogger(ApplicationStartupRegistrar.class);

    public ApplicationStartupRegistrar(DiscoveryProperties props,
                                       RestTemplate rest) {
        this.props = props;
        this.rest = rest;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            Map<String, Object> body = Map.of("serviceName",
                    props.getServiceName(), "port", props.getServicePort());
            String url = props.getServerUrl().replaceAll("/$", "") + "/register";
            rest.postForEntity(url, body, Map.class);
        } catch (Exception ex) {
            log.error("Discovery registration failed: {}", ex.getMessage(), ex);
        }
    }
}
