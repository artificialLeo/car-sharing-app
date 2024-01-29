package com.app.car.model;

import com.app.car.model.enums.CarType;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;

    private String brand;

    @Enumerated
    private CarType type;

    private int inventory;

    private BigDecimal dailyFee;
}

