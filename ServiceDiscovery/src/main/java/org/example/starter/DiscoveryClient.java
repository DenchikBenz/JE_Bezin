package org.example.starter;

import org.example.starter.properties.DiscoveryProperties;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

public class DiscoveryClient {
    private final DiscoveryProperties props;
    private final RestTemplate rest;

    public DiscoveryClient(DiscoveryProperties props, RestTemplate rest) {
        this.props = props;
        this.rest = rest;
    }

    public URI getInstance(String serviceName) {
        String url = props.getServerUrl().replaceAll("/$", "") + "/discover/"
                + serviceName;
        try {
            var resp = rest.getForEntity(url, Map.class);
            if (!resp.getStatusCode().is2xxSuccessful()) return null;
            Map body = resp.getBody();
            String host = (String) body.get("host");
            Integer port = (Integer) body.get("port");
            return new URI("http", null, host, port, null, null, null);
        } catch (RestClientException | java.net.URISyntaxException ex) {
            throw new RuntimeException("Cannot get instance for " +
                    serviceName, ex);
        }
    }
}
