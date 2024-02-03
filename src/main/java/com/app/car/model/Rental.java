package com.app.car.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate rentalDate;

    private LocalDate returnDate;

    private LocalDate actualReturnDate;

    @ManyToOne
    private Car car;

    @ManyToOne
    private User user;
}
