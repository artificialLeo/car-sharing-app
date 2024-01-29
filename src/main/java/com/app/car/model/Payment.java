package com.app.car.model;

import com.app.car.model.enums.PaymentStatus;
import com.app.car.model.enums.PaymentType;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    private PaymentStatus status;

    @Enumerated
    private PaymentType type;

    @ManyToOne
    private Rental rental;

    private String sessionUrl;

    private String sessionId;

    private BigDecimal amountToPay;
}
