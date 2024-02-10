package com.app.car.repository;

import com.app.car.TestContainerManager;
import com.app.car.model.Car;
import com.app.car.model.Payment;
import com.app.car.model.Rental;
import com.app.car.model.User;
import com.app.car.model.enums.CarType;
import com.app.car.model.enums.PaymentStatus;
import com.app.car.model.enums.PaymentType;
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

    private User user;
    private Car car;
    private Rental rental;
    private Payment payment1;
    private Payment payment2;

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

        rental = Rental.builder()
                .rentalDate(LocalDate.now().minusDays(5))
                .car(car)
                .user(user)
                .build();
        rentalRepository.save(rental);

        payment1 = Payment.builder()
                .status(PaymentStatus.PAID)
                .type(PaymentType.PAYMENT)
                .rental(rental)
                .sessionUrl("http://payment-url.com")
                .sessionId("payment_session_id")
                .amountToPay(BigDecimal.valueOf(200.00))
                .build();

        payment2 = Payment.builder().rental(rental).build();

        paymentRepository.saveAll(List.of(payment1, payment2));
    }

    @AfterEach
    void destroy() {
        carRepository.deleteAll();
        paymentRepository.deleteAll();
        rentalRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("findBySessionId -> Existing Session ID")
    public void findBySessionId_ExistingSessionId_ReturnPayment() {
        Payment foundPayment = paymentRepository.findBySessionId(payment1.getSessionId());

        assertNotNull(foundPayment);
        assertEquals(payment1.getId(), foundPayment.getId());
    }

    @Test
    @DisplayName("findBySessionId -> Nonexistent Session ID")
    public void findBySessionId_NonexistentSessionId_ReturnNull() {
        Payment foundPayment = paymentRepository.findBySessionId("nonexistent_session_id");

        assertNull(foundPayment);
    }

    @Test
    @DisplayName("findByRentalId -> Existing Rental ID")
    public void findByRentalId_ExistingRentalId_ReturnPayments() {
        List<Payment> payments = paymentRepository.findByRentalId(rental.getId());

        assertEquals(2, payments.size());
    }

    @Test
    @DisplayName("findByRentalId -> Nonexistent Rental ID")
    public void findByRentalId_NonexistentRentalId_ReturnEmptyList() {
        List<Payment> payments = paymentRepository.findByRentalId(999L);

        assertTrue(payments.isEmpty());
    }
}
