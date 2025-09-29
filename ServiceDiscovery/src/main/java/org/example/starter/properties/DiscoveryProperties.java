package org.example.starter.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "discovery")
@Getter
@Setter
public class DiscoveryProperties {
    private String serverUrl;
    private String serviceName;
    private Integer servicePort;
    private String healthPath = "/health";
}
