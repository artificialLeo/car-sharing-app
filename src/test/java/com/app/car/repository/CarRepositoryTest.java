package com.app.car.repository;

import com.app.car.TestContainerManager;
import com.app.car.model.Car;
import com.app.car.model.enums.CarType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = TestContainerManager.class)
class CarRepositoryTest {
    @Autowired
    CarRepository carRepository;

    private Car car1;

    private Car car2;

    @BeforeEach
    void init() {
        car1 = Car.builder()
                .model("Model X")
                .brand("Tesla")
                .type(CarType.SUV)
                .inventory(5)
                .dailyFee(BigDecimal.valueOf(100.00))
                .build();

        car2 = Car.builder()
                .model("Civic")
                .brand("Honda")
                .type(CarType.SEDAN)
                .inventory(3)
                .dailyFee(BigDecimal.valueOf(80.00))
                .build();

        carRepository.saveAll(List.of(car1, car2));
    }

    @AfterEach
    void destroy() {
        carRepository.deleteAll();
    }

    @Test
    @DisplayName("findAllBy -> Return CarShortInfoDto Page")
    public void findAllBy_ReturnCarShortInfoDtoPage() {
        Page<Car> carPage = carRepository.findAllBy(PageRequest.of(0, 10));

        assertNotNull(carPage);
        assertEquals(2, carPage.getTotalElements());
        assertEquals(2, carPage.getContent().size());
    }
}
