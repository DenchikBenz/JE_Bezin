package org.example.servicediscoveryspring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String serviceName;
    private int port;

}
