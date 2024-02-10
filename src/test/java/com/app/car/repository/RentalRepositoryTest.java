package com.app.car.repository;

import com.app.car.TestContainerManager;
import com.app.car.model.Car;
import com.app.car.model.Rental;
import com.app.car.model.User;
import com.app.car.model.enums.CarType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    private User user;
    private Car car;
    private Rental rental1;
    private Rental rental2;

    @BeforeEach
    void init() {
        user = User.builder().email("test@example.com").build();
        userRepository.save(user);

        car = Car.builder()
                .model("Model X")
                .brand("Tesla")
                .type(CarType.SUV)
                .inventory(1)
                .dailyFee(BigDecimal.valueOf(100.00))
                .build();
        carRepository.save(car);

        rental1 = Rental.builder()
                .rentalDate(LocalDate.now().minusDays(4))
                .returnDate(LocalDate.now().minusDays(2))
                .car(car)
                .user(user)
                .build();
        rentalRepository.save(rental1);

        rental2 = Rental.builder()
                .rentalDate(LocalDate.now().minusDays(2))
                .car(car)
                .user(user)
                .build();
        rentalRepository.save(rental2);
    }

    @AfterEach
    void destroy() {
        rentalRepository.deleteAll();
        userRepository.deleteAll();
        carRepository.deleteAll();
    }

    @Test
    @DisplayName("findByUserIdAndActualReturnDateIsNull -> Existing User, No Rentals")
    public void findByUserIdAndActualReturnDateIsNull_ExistingUserNoRentals_ReturnEmptyList() {
        List<Rental> rentals = rentalRepository.findByUserIdAndActualReturnDateIsNull(user.getId(), true);

        boolean expected = rentals.isEmpty();
        assertTrue(expected);
    }

    @Test
    @DisplayName("findByUserIdAndActualReturnDateIsNull -> Existing User, Active Rentals")
    public void findByUserIdAndActualReturnDateIsNull_ExistingUserActiveRentals_ReturnRentals() {
        List<Rental> rentals = rentalRepository.findByUserIdAndActualReturnDateIsNull(user.getId(), false);

        int expected = 2;
        int actual = rentals.size();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findByCar_IdAndActualReturnDateIsNull -> Active Rentals for Car")
    public void findByCar_IdAndActualReturnDateIsNull_ActiveRentalsForCar_ReturnRentals() {
        List<Rental> rentals = rentalRepository.findByCar_IdAndActualReturnDateIsNull(car.getId());

        int expected = 2;
        int actual = rentals.size();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findByActualReturnDateBeforeAndActualReturnDateIsNull -> Rentals Overdue")
    public void findByActualReturnDateBeforeAndActualReturnDateIsNull_RentalsOverdue_ReturnRentals() {
        List<Rental> rentals = rentalRepository.findByReturnDateBeforeAndActualReturnDateIsNull(LocalDate.now().minusDays(1));

        int expected = 1;
        int actual = rentals.size();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findByUserId -> All Rentals for User")
    public void findByUserId_AllRentalsForUser_ReturnRentals() {
        List<Rental> rentals = rentalRepository.findByUserId(user.getId());

        int expected = 2;
        int actual = rentals.size();
        assertEquals(expected, actual);
    }
}
