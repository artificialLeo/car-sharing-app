package com.app.car.repository;

import com.app.car.config.TestContainerManager;
import com.app.car.model.Car;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainerManager.class)
class CarRepositoryTest extends TestContainerManager {
    @Test
    @DisplayName("Return all CarShortInfoDto Page")
    public void findAllBy_ReturnCarShortInfoDtoPage() {
        Page<Car> actual = carRepository.findAllBy(PageRequest.of(0, 10));

        assertNotNull(actual);
        assertEquals(2, actual.getTotalElements());
        assertEquals(2, actual.getContent().size());
    }
}
