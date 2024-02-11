package com.app.car.dto.car;

import com.app.car.model.enums.CarType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarUpdateDto {
    @NotBlank(message = "Model cannot be blank")
    private String model;

    @NotBlank(message = "Brand cannot be blank")
    private String brand;

    @NotNull(message = "Type cannot be null")
    private CarType type;

    @NotNull(message = "DailyFee cannot be null")
    private BigDecimal dailyFee;
}
