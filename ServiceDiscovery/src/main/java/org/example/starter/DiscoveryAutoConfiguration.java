package org.example.starter;


import org.example.starter.properties.DiscoveryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(DiscoveryProperties.class)
@ConditionalOnProperty(prefix = "discovery", name = "server-url")
public class DiscoveryAutoConfiguration {
    @Bean
    public RestTemplate discoveryRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public DiscoveryClient discoveryClient(DiscoveryProperties props,
                                           RestTemplate rest) {
        return new DiscoveryClient(props, rest);
    }


    @Bean
    public ApplicationStartupRegistrar
    applicationStartupRegistrar(DiscoveryProperties props, RestTemplate rest) {
        return new ApplicationStartupRegistrar(props, rest);
    }

}
