package org.example.servicediscoveryspring.registry;

import org.example.servicediscoveryspring.entity.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ServiceRegistry {

    private static class Group {
        final CopyOnWriteArrayList<ServiceInstance> instances = new
                CopyOnWriteArrayList<>();
        final AtomicInteger counter = new AtomicInteger(0);
    }

    private final ConcurrentHashMap<String, Group> map = new
            ConcurrentHashMap<>();

    public void register(String serviceName, ServiceInstance instance) {
        Group g = map.computeIfAbsent(serviceName, k -> new Group());
        boolean exists = g.instances.stream().anyMatch(i ->
                i.host().equals(instance.host()) && i.port() == instance.port());
        if (!exists) g.instances.add(instance);
    }

    public Optional<ServiceInstance> discover(String serviceName) {
        Group g = map.get(serviceName);
        if (g == null || g.instances.isEmpty()) return Optional.empty();
        int idx = Math.abs(g.counter.getAndIncrement());
        ServiceInstance inst = g.instances.get(idx % g.instances.size());
        return Optional.of(inst);
    }

    public Map<String, List<ServiceInstance>> snapshot() {
        Map<String, List<ServiceInstance>> res = new HashMap<>();
        for (Map.Entry<String, Group> e : map.entrySet()) {
            res.put(e.getKey(), new ArrayList<>(e.getValue().instances));
        }
        return res;
    }

    public void remove(String serviceName, ServiceInstance instance) {
        Group g = map.get(serviceName);
        if (g != null) {
            g.instances.removeIf(i -> i.host().equals(instance.host())
                    && i.port() == instance.port());
        }
    }
}
