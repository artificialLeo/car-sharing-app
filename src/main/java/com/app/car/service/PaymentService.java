package com.app.car.service;

import com.app.car.dto.payment.PaymentRequestDto;
import com.app.car.dto.payment.PaymentResponseDto;
import java.util.List;

public interface PaymentService {
    PaymentResponseDto createPaymentSession(PaymentRequestDto paymentRequest);

    void handleSuccessfulPayment(String sessionId);

    void handleCancelledPayment(String sessionId);

    List<PaymentResponseDto> getUserPayments(Long userId);
}
