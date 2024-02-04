package com.app.car.dto.rental;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalDto {

    private Long id;

    @NotNull(message = "Rental date cannot be null")
    private LocalDate rentalDate;

    private LocalDate returnDate;

    private Long carId;

    private Long userId;
}


