package com.app.car.dto.rental;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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
