package com.app.car.repository;

import com.app.car.TestContainerManager;
import com.app.car.model.Car;
import com.app.car.model.Payment;
import com.app.car.model.Rental;
import com.app.car.model.User;
import com.app.car.model.enums.CarType;
import com.app.car.model.enums.PaymentStatus;
import com.app.car.model.enums.PaymentType;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = TestContainerManager.class)
public class PaymentRepositoryTest {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    CarRepository carRepository;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
        rentalRepository.deleteAll();
        carRepository.deleteAll();
    }

    @Test
    @DisplayName("findBySessionId -> Existing Session ID")
    public void findBySessionId_ExistingSessionId_ReturnPayment() {
        // Given
        Rental rental = createRental();
        Payment payment = createPayment(rental);
        paymentRepository.save(payment);

        // When
        Payment foundPayment = paymentRepository.findBySessionId(payment.getSessionId());

        // Then
        assertNotNull(foundPayment);
        assertEquals(payment.getId(), foundPayment.getId());
    }

    @Test
    @DisplayName("findBySessionId -> Nonexistent Session ID")
    public void findBySessionId_NonexistentSessionId_ReturnNull() {
        // When
        Payment foundPayment = paymentRepository.findBySessionId("nonexistent_session_id");

        // Then
        assertNull(foundPayment);
    }

    @Test
    @DisplayName("findByRentalId -> Existing Rental ID")
    public void findByRentalId_ExistingRentalId_ReturnPayments() {
        // Given
        Rental rental = createRental();
        Payment payment1 = createPayment(rental);
        Payment payment2 = createPayment(rental);
        paymentRepository.saveAll(List.of(payment1, payment2));

        // When
        List<Payment> payments = paymentRepository.findByRentalId(rental.getId());

        // Then
        assertEquals(2, payments.size());
    }

    @Test
    @DisplayName("findByRentalId -> Nonexistent Rental ID")
    public void findByRentalId_NonexistentRentalId_ReturnEmptyList() {
        // When
        List<Payment> payments = paymentRepository.findByRentalId(999L);

        // Then
        assertTrue(payments.isEmpty());
    }

    private Rental createRental() {
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

        Rental rental = new Rental();
        rental.setRentalDate(LocalDate.now().minusDays(5));
        rental.setCar(car);
        rental.setUser(user);
        return rentalRepository.save(rental);
    }

    private Payment createPayment(Rental rental) {
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.PAID);
        payment.setType(PaymentType.PAYMENT);
        payment.setRental(rental);
        payment.setSessionUrl("http://payment-url.com");
        payment.setSessionId("payment_session_id");
        payment.setAmountToPay(BigDecimal.valueOf(200.00));
        return payment;
    }

}
