package com.app.car.repository;

import com.app.car.config.TestContainerManager;
import com.app.car.model.Rental;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainerManager.class)
public class RentalRepositoryTest extends TestContainerManager {
    @Test
    @DisplayName("Existing User, No Rentals")
    public void findByUserIdAndActualReturnDateIsNull_ExistingUserNoRentals_ReturnEmptyList() {
        List<Rental> rentals = rentalRepository.findByUserIdAndActualReturnDateIsNull(customer.getId(), true);

        boolean expected = rentals.isEmpty();
        assertTrue(expected);
    }

    @Test
    @DisplayName("Existing User, Active Rentals")
    public void findByUserIdAndActualReturnDateIsNull_ExistingUserActiveRentals_ReturnRentals() {
        List<Rental> rentals = rentalRepository.findByUserIdAndActualReturnDateIsNull(customer.getId(), false);

        int expected = 1;
        int actual = rentals.size();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Active Rentals for Car")
    public void findByCar_IdAndActualReturnDateIsNull_ActiveRentalsForCar_ReturnRentals() {
        List<Rental> rentals = rentalRepository.findByCar_IdAndActualReturnDateIsNull(car.getId());

        int expected = 1;
        int actual = rentals.size();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Rentals Overdue")
    public void findByActualReturnDateBeforeAndActualReturnDateIsNull_RentalsOverdue_ReturnRentals() {
        List<Rental> rentals = rentalRepository
                .findByReturnDateBeforeAndActualReturnDateIsNull(
                        LocalDate
                                .now()
                                .minusDays(4)
                );

        int expected = 1;
        int actual = rentals.size();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("All Rentals for User")
    public void findByUserId_AllRentalsForUser_ReturnRentals() {
        List<Rental> rentals = rentalRepository.findByUserId(customer.getId());

        int expected = 1;
        int actual = rentals.size();
        assertEquals(expected, actual);
    }
}
