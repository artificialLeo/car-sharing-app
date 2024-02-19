package com.app.car.config;

import com.app.car.util.HealthCheck;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(locations = "classpath:/application-context.xml")
public class HealthCheckTest {
    @Autowired
    HealthCheck healthCheck;

    @Test
    void health_UpWhenNoError() {
        healthCheck = new HealthCheck();

        Health result = healthCheck.health();

        assertEquals(Health.up().build(), result);
    }

    @Test
    void health_DownWhenErrorCodePresent() {
        healthCheck = new HealthCheck() {
            @Override
            public int check() {
                return 1;
            }
        };

        Health result = healthCheck.health();

        assertEquals(Health.down().withDetail("Error Code", 1).build(), result);
    }
}
