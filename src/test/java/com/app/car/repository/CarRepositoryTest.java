package com.app.car.repository;

import com.app.car.TestContainerManager;
import com.app.car.dto.car.CarShortInfoDto;
import com.app.car.model.Car;
import com.app.car.model.enums.CarType;
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

    @BeforeEach
    void setUp() {
        carRepository.deleteAll();
    }

    @Test
    @DisplayName("findAllBy -> Return CarShortInfoDto Page")
    public void findAllBy_ReturnCarShortInfoDtoPage() {
        // Given
        Car car1 = createCar("Model X", "Tesla", CarType.SUV, 5, BigDecimal.valueOf(100.00));
        Car car2 = createCar("Civic", "Honda", CarType.SEDAN, 3, BigDecimal.valueOf(80.00));
        carRepository.saveAll(List.of(car1, car2));

        // When
        Page<Car> carPage = carRepository.findAllBy(PageRequest.of(0, 10));



        // Then
        assertNotNull(carPage);
        assertEquals(2, carPage.getTotalElements());
        assertEquals(2, carPage.getContent().size());
        // Additional assertions based on your specific requirements
    }

    // Additional test methods can be added for other repository methods

    private Car createCar(String model, String brand, CarType type, int inventory, BigDecimal dailyFee) {
        Car car = new Car();
        car.setModel(model);
        car.setBrand(brand);
        car.setType(type);
        car.setInventory(inventory);
        car.setDailyFee(dailyFee);
        return car;
    }
}
