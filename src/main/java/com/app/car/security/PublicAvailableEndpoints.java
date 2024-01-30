package com.app.car.security;

import lombok.Getter;

import java.util.List;

public class PublicAvailableEndpoints {
    @Getter
    private static final List<String> publicEndpoints = List.of(
            "/api/auth/login",
            "/api/auth/registration"
    );

}
