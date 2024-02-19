package com.app.car.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OpenEndpoints {
    AUTH("/auth", "/auth/**"),
    ACTUATOR("/actuator", "/actuator/**"),
    V3("/v3", "/v3/**"),
    SWAGGER("/swagger-ui", "/swagger-ui/**"),
    ERROR("/error", "/error/**"),
    ;

    private final String path;
    private final String pattern;
}
