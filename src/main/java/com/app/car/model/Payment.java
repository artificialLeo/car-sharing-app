package com.app.car.model;

import com.app.car.model.enums.PaymentStatus;
import com.app.car.model.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentType type;

    @ManyToOne
    private Rental rental;

    private String sessionUrl;

    private String sessionId;

    private BigDecimal amountToPay;
}
