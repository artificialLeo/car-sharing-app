package com.app.car.repository;

import com.app.car.config.TestContainerManager;
import com.app.car.model.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainerManager.class)
public class PaymentRepositoryTest extends TestContainerManager {
    @Test
    @DisplayName("findBySessionId -> Existing Session ID")
    public void findBySessionId_ExistingSessionId_ReturnPayment() {
        Payment actual = paymentRepository.findBySessionId(payment.getSessionId());

        assertNotNull(actual);
        assertEquals(payment.getId(), actual.getId());
    }

    @Test
    @DisplayName("findBySessionId -> Nonexistent Session ID")
    public void findBySessionId_NonexistentSessionId_ReturnNull() {
        Payment actual = paymentRepository.findBySessionId("nonexistent_session_id");

        assertNull(actual);
    }

    @Test
    @DisplayName("findByRentalId -> Existing Rental ID")
    public void findByRentalId_ExistingRentalId_ReturnPayments() {
        List<Payment> actual = paymentRepository.findByRentalId(rental.getId());

        assertEquals(1, actual.size());
    }

    @Test
    @DisplayName("findByRentalId -> Nonexistent Rental ID")
    public void findByRentalId_NonexistentRentalId_ReturnEmptyList() {
        List<Payment> actual = paymentRepository.findByRentalId(999L);

        assertTrue(actual.isEmpty());
    }
}
