package com.app.car.service.impl;

import com.app.car.dto.payment.PaymentRequestDto;
import com.app.car.dto.payment.PaymentResponseDto;
import com.app.car.mapper.PaymentMapper;
import com.app.car.model.Payment;
import com.app.car.model.Rental;
import com.app.car.model.User;
import com.app.car.model.enums.PaymentStatus;
import com.app.car.model.enums.PaymentType;
import com.app.car.repository.PaymentRepository;
import com.app.car.repository.RentalRepository;
import com.app.car.service.PaymentService;
import com.app.car.util.StripeSessionUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final StripeSessionUtil stripeSessionUtil;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentResponseDto createPaymentSession(PaymentRequestDto paymentRequest) {
        Long rentalId = paymentRequest.getRentalId();
        PaymentType paymentType = paymentRequest.getPaymentType();
        BigDecimal unitAmount = calculateTotalPrice(rentalId);

        String successUrl = buildSuccessUrl(rentalId);
        String cancelUrl = buildCancelUrl(rentalId);

        String sessionUrl = stripeSessionUtil.createPaymentSession(
                successUrl,
                cancelUrl,
                rentalId,
                paymentType,
                unitAmount
        );

        Rental rental = rentalRepository.findById(rentalId).orElse(null);

        if (rental != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (authentication != null) ? (User) authentication.getPrincipal() : null;

            rental.setUser(user);

            rentalRepository.save(rental);

            Payment payment = new Payment();
            payment.setRental(rental);
            payment.setSessionUrl(sessionUrl);
            payment.setSessionId("");
            paymentRepository.save(payment);

            return paymentMapper.toDto(payment);
        } else {
            return null;
        }
    }

    @Override
    public void handleSuccessfulPayment(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId);
        if (payment != null) {
            payment.setStatus(PaymentStatus.PAID);
            paymentRepository.save(payment);
        }
    }

    @Override
    public void handleCancelledPayment(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId);
        if (payment != null) {
            payment.setStatus(PaymentStatus.CANCELLED);
            paymentRepository.save(payment);
        }
    }

    @Override
    public List<PaymentResponseDto> getUserPayments(Long userId) {
        return rentalRepository
                .findByUserId(userId)
                .stream()
                .flatMap(rental -> paymentRepository.findByRentalId(rental.getId()).stream())
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

    public BigDecimal calculateTotalPrice(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId).orElse(null);

        if (rental != null && rental.getReturnDate() != null && rental.getActualReturnDate() != null) {
            LocalDate returnDate = rental.getReturnDate();
            LocalDate actualReturnDate = rental.getActualReturnDate();

            long overdueDays = ChronoUnit.DAYS.between(returnDate, actualReturnDate);
            BigDecimal dailyFee = BigDecimal.valueOf(20);
            BigDecimal fineMultiplier = BigDecimal.valueOf(1.5);

            BigDecimal totalPrice = BigDecimal.ZERO;

            if (overdueDays > 0) {
                totalPrice = dailyFee.multiply(BigDecimal.valueOf(overdueDays)).multiply(fineMultiplier);
            }

            return totalPrice;
        } else {
            return BigDecimal.ZERO;
        }
    }


    public String buildSuccessUrl(Long rentalId) {
        return "/payments/success?rentalId=" + rentalId;
    }

    public String buildCancelUrl(Long rentalId) {
        return "/payments/cancel?rentalId=" + rentalId;
    }
}
