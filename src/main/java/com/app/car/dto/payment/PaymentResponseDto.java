package com.app.car.dto.payment;

import com.app.car.model.enums.PaymentStatus;
import com.app.car.model.enums.PaymentType;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {
    private Long id;
    private PaymentStatus status;
    private PaymentType type;
    private String sessionUrl;
    private String sessionId;
    private BigDecimal amountToPay;
}
