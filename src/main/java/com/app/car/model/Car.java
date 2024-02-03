package com.app.car.model;

import com.app.car.model.enums.CarType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;

    private String brand;

    @Enumerated(EnumType.STRING)
    private CarType type;

    private int inventory;

    private BigDecimal dailyFee;
}
