package com.app.car.security;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationToken implements Authentication {
    private final String username;
    private final String password;

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }
}
