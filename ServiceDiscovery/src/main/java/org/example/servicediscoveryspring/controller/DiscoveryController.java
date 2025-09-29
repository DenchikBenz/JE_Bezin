package org.example.servicediscoveryspring.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.example.servicediscoveryspring.dto.RegisterRequest;
import org.example.servicediscoveryspring.dto.ResponseDto;
import org.example.servicediscoveryspring.entity.ServiceInstance;
import org.example.servicediscoveryspring.registry.ServiceRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class DiscoveryController {
    private final ServiceRegistry registry;

    public DiscoveryController(ServiceRegistry registry) {
        this.registry =
                registry;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req,
                                      HttpServletRequest request) {
        String host = extractHost(request);
        ServiceInstance inst = new ServiceInstance(host, req.getPort());
        registry.register(req.getServiceName(), inst);
        ResponseDto dto = new ResponseDto("registered");
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/discover/{serviceName}")
    public ResponseEntity<?> discover(@PathVariable String serviceName) {
        return registry.discover(serviceName)
                .map(inst -> ResponseEntity.ok(Map.of("host",
                        inst.host(), "port", inst.port())))
                .orElseGet(() ->
                        ResponseEntity.status(404).body(Map.of("error", "not_found")));
    }

    private String extractHost(HttpServletRequest request) {
        return request.getRemoteHost();
    }
}
