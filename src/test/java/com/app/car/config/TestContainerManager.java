package com.app.car.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.MySQLContainer;

public class TestContainerManager extends TestDatabaseInit implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    static final MySQLContainer<?> selfMySQLContainer = new MySQLContainer<>("mysql:8.0.36");

    static {
        selfMySQLContainer.start();
        registerShutdownHook();
    }

    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(selfMySQLContainer::stop));
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
