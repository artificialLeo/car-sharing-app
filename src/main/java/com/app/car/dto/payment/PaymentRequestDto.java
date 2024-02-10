package com.app.car.dto.payment;

import com.app.car.model.enums.PaymentType;
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
public class PaymentRequestDto {

    @NotNull(message = "Rental ID cannot be null")
    private Long rentalId;

    @NotNull(message = "Payment type cannot be null")
    private PaymentType paymentType;

    @NotNull(message = "Payment type cannot be null")
    private BigDecimal paymentAmount;
}
