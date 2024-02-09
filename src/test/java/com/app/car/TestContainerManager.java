package com.app.car;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.MySQLContainer;

public class TestContainerManager implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Getter
    private static MySQLContainer<?> selfMySQLContainer = new MySQLContainer<>("mysql:8.0.36");

    static {
        selfMySQLContainer.start();
    }

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.url=" + selfMySQLContainer.getJdbcUrl(),
                "spring.datasource.username=" + selfMySQLContainer.getUsername(),
                "spring.datasource.password=" + selfMySQLContainer.getPassword()
        );
    }

}



