package com.app.car.mapper;

import com.app.car.dto.payment.PaymentResponseDto;
import com.app.car.model.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponseDto toDto(Payment payment);
}
