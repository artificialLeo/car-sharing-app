package com.app.car.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping
    public String healthCheck() {
        return "OK";
    }
}
