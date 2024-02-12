package com.app.car.service;

import com.app.car.dto.payment.PaymentRequestDto;
import com.app.car.dto.payment.PaymentResponseDto;
import com.app.car.exception.MockException;
import com.app.car.mapper.PaymentMapper;
import com.app.car.model.Payment;
import com.app.car.model.Rental;
import com.app.car.model.enums.PaymentStatus;
import com.app.car.model.enums.PaymentType;
import com.app.car.repository.PaymentRepository;
import com.app.car.repository.RentalRepository;
import com.app.car.service.impl.PaymentServiceImpl;
import com.app.car.util.StripeSessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PaymentServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private StripeSessionUtil stripeSessionUtil;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void init() {
        try {
            MockitoAnnotations.openMocks(this);
        } catch (Exception e) {
            throw new MockException("Error initializing mocks : " + e);
        }
    }

    @Test
    @DisplayName("createPaymentSession success")
    void createPaymentSession_ValidPaymentRequest_Success() {
        PaymentRequestDto paymentRequest = new PaymentRequestDto();
        paymentRequest.setRentalId(1L);
        paymentRequest.setPaymentType(PaymentType.PAYMENT);

        Rental rental = new Rental();
        rental.setId(1L);

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(stripeSessionUtil.createPaymentSession(any(), any(), any(), any(), any())).thenReturn("sessionUrl");
        when(paymentRepository.save(any())).thenReturn(new Payment());
        when(paymentMapper.toDto(any())).thenReturn(new PaymentResponseDto());

        PaymentResponseDto result = paymentService.createPaymentSession(paymentRequest);

        assertNotNull(result);

        verify(rentalRepository, times(2)).findById(1L);

        verify(stripeSessionUtil, times(1)).createPaymentSession(any(), any(), any(), any(), any());
        verify(paymentRepository, times(1)).save(any());
        verify(paymentMapper, times(1)).toDto(any());
    }


    @Test
    @DisplayName("handleSuccessfulPayment success")
    void handleSuccessfulPayment_ValidSessionId_Success() {
        String sessionId = "validSessionId";
        Payment payment = new Payment();

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(payment);
        when(paymentRepository.save(any())).thenReturn(payment);

        paymentService.handleSuccessfulPayment(sessionId);

        assertEquals(PaymentStatus.PAID, payment.getStatus());
        verify(paymentRepository, times(1)).findBySessionId(sessionId);
        verify(paymentRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("handleCancelledPayment success")
    void handleCancelledPayment_ValidSessionId_Success() {
        String sessionId = "validSessionId";
        Payment payment = new Payment();

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(payment);
        when(paymentRepository.save(any())).thenReturn(payment);

        paymentService.handleCancelledPayment(sessionId);

        assertEquals(PaymentStatus.CANCELLED, payment.getStatus());
        verify(paymentRepository, times(1)).findBySessionId(sessionId);
        verify(paymentRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("getUserPayments success")
    void getUserPayments_ValidUserId_Success() {
        Long userId = 1L;
        Rental rental = new Rental();
        rental.setId(1L);

        when(rentalRepository.findByUserId(userId)).thenReturn(Collections.singletonList(rental));
        when(paymentRepository.findByRentalId(rental.getId())).thenReturn(Collections.singletonList(new Payment()));
        when(paymentMapper.toDto(any())).thenReturn(new PaymentResponseDto());

        List<PaymentResponseDto> result = paymentService.getUserPayments(userId);

        assertFalse(result.isEmpty());
        verify(rentalRepository, times(1)).findByUserId(userId);
        verify(paymentRepository, times(1)).findByRentalId(rental.getId());
        verify(paymentMapper, times(1)).toDto(any());
    }

    @Test
    @DisplayName("calculateTotalPrice success")
    void calculateTotalPrice_ValidRentalId_ReturnsTotalPrice() {
        Long rentalId = 1L;
        Rental rental = new Rental();
        rental.setId(rentalId);
        rental.setReturnDate(LocalDate.now().minusDays(5));
        rental.setActualReturnDate(LocalDate.now());

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        BigDecimal result = paymentService.calculateTotalPrice(rentalId);

        assertEquals(BigDecimal.valueOf(150), result.setScale(0, RoundingMode.HALF_UP));
        verify(rentalRepository, times(1)).findById(rentalId);
    }

    @Test
    @DisplayName("calculateTotalPrice success 0")
    void calculateTotalPrice_InvalidRentalId_ReturnsZero() {
        Long rentalId = 1L;

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        BigDecimal result = paymentService.calculateTotalPrice(rentalId);

        assertEquals(BigDecimal.ZERO, result);
        verify(rentalRepository, times(1)).findById(rentalId);
    }

    @Test
    @DisplayName("buildSuccessUrl success")
    void buildSuccessUrl_ValidRentalId_ReturnsSuccessUrl() {
        Long rentalId = 1L;
        String result = paymentService.buildSuccessUrl(rentalId);
        assertEquals("/payments/success?rentalId=1", result);
    }

    @Test
    @DisplayName("buildCancelUrl success")
    void buildCancelUrl_ValidRentalId_ReturnsCancelUrl() {
        Long rentalId = 1L;
        String result = paymentService.buildCancelUrl(rentalId);
        assertEquals("/payments/cancel?rentalId=1", result);
    }
}

