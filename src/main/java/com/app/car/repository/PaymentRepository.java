package com.app.car.repository;

import com.app.car.model.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findBySessionId(String sessionId);

    List<Payment> findByRentalId(Long id);
}
