package com.app.car.repository;

import com.app.car.TestContainerManager;
import com.app.car.model.Car;
import com.app.car.model.Rental;
import com.app.car.model.User;
import com.app.car.model.enums.CarType;
import com.app.car.repository.CarRepository;
import com.app.car.repository.RentalRepository;
import com.app.car.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = TestContainerManager.class)
public class RentalRepositoryTest {

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CarRepository carRepository;

    @BeforeEach
    void setUp() {
        rentalRepository.deleteAll();
        userRepository.deleteAll();
        carRepository.deleteAll();
    }

    @Test
    @DisplayName("findByUserIdAndActualReturnDateIsNull -> Existing User, No Rentals")
    public void findByUserIdAndActualReturnDateIsNull_ExistingUserNoRentals_ReturnEmptyList() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        userRepository.save(user);

        // When
        List<Rental> rentals = rentalRepository.findByUserIdAndActualReturnDateIsNull(user.getId(), true);

        // Then
        assertTrue(rentals.isEmpty());
    }

    @Test
    @DisplayName("findByUserIdAndActualReturnDateIsNull -> Existing User, Active Rentals")
    public void findByUserIdAndActualReturnDateIsNull_ExistingUserActiveRentals_ReturnRentals() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        userRepository.save(user);

        Car car = new Car();
        car.setModel("Model X");
        car.setBrand("Tesla");
        car.setType(CarType.SUV);
        car.setInventory(1);
        car.setDailyFee(BigDecimal.valueOf(100.00));
        carRepository.save(car);

        Rental rental1 = new Rental();
        rental1.setRentalDate(LocalDate.now().minusDays(5));
        rental1.setCar(car);
        rental1.setUser(user);
        rentalRepository.save(rental1);

        Rental rental2 = new Rental();
        rental2.setRentalDate(LocalDate.now().minusDays(2));
        rental2.setCar(car);
        rental2.setUser(user);
        rentalRepository.save(rental2);

        // When
        List<Rental> rentals = rentalRepository.findByUserIdAndActualReturnDateIsNull(user.getId(), false);

        // Then
        assertEquals(2, rentals.size());
    }

    // Repeat a similar structure for the other test methods
}

