package com.app.car.controller;

import com.app.car.dto.payment.PaymentRequestDto;
import com.app.car.dto.payment.PaymentResponseDto;
import com.app.car.service.PaymentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentsController {
    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> getUserPayments(@RequestParam Long userId) {
        List<PaymentResponseDto> userPayments = paymentService.getUserPayments(userId);
        return ResponseEntity.ok(userPayments);
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPaymentSession(
            @Valid @RequestBody PaymentRequestDto paymentRequest
    ) {
        PaymentResponseDto paymentResponse = paymentService.createPaymentSession(paymentRequest);
        if (paymentResponse != null) {
            return ResponseEntity.ok(paymentResponse);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/success")
    public ResponseEntity<String> checkSuccessfulPayments(@RequestParam String sessionId) {
        paymentService.handleSuccessfulPayment(sessionId);
        final String successfulPayment = "Payment was successful!";
        return ResponseEntity.ok(successfulPayment);
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> returnPaymentPausedMessage(@RequestParam String sessionId) {
        paymentService.handleCancelledPayment(sessionId);
        final String cancelledPayment = "Payment has been cancelled!";
        return ResponseEntity.ok(cancelledPayment);
    }

}
