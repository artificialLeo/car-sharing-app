package com.app.car.service;

import com.app.car.dto.car.CarUpdateDto;
import com.app.car.dto.rental.CompletedRentalDto;
import com.app.car.dto.rental.RentalDto;
import com.app.car.exception.CarAlreadyRentedException;
import com.app.car.exception.InsufficientInventoryException;
import com.app.car.exception.MockException;
import com.app.car.exception.NoRentalsFoundException;
import com.app.car.exception.RentalNotFoundException;
import com.app.car.exception.RentalReturnedException;
import com.app.car.mapper.CarMapper;
import com.app.car.mapper.PaymentMapper;
import com.app.car.mapper.RentalMapper;
import com.app.car.model.Car;
import com.app.car.model.Rental;
import com.app.car.repository.PaymentRepository;
import com.app.car.repository.RentalRepository;
import com.app.car.service.impl.PaymentServiceImpl;
import com.app.car.service.impl.RentalServiceImpl;
import com.app.car.service.impl.TelegramNotificationServiceImpl;
import com.app.car.util.StripeSessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RentalServiceImplTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private CarService carService;

    @Mock
    private TelegramNotificationServiceImpl telegramNotificationService;

    @Mock
    private RentalMapper rentalMapper;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private RentalServiceImpl rentalService;

    @BeforeEach
    void init() {
        try {
            MockitoAnnotations.openMocks(this);
        } catch (Exception e) {
            throw new MockException("Error initializing mocks : " + e);
        }
    }

    @Test
    @DisplayName("addRental -> Success")
    void addRental_WithAvailableInventory_Success() {
        RentalDto rentalDto = new RentalDto();
        rentalDto.setCarId(1L);

        Car car = new Car();
        car.setId(1L);
        car.setInventory(1);

        when(carService.getCarById(1L)).thenReturn(car);
        when(rentalRepository.findByCar_IdAndActualReturnDateIsNull(1L)).thenReturn(Collections.emptyList());
        when(carMapper.toCarUpdateDto(car)).thenReturn(new CarUpdateDto());
        when(rentalMapper.toEntity(rentalDto)).thenReturn(new Rental());
        when(rentalRepository.save(any())).thenReturn(new Rental());
        when(rentalMapper.toDto(any())).thenReturn(new RentalDto());

        RentalDto result = rentalService.addRental(rentalDto);

        assertNotNull(result);
        assertEquals(0, car.getInventory());
        verify(carService, times(1)).updateCar(1L, new CarUpdateDto());
        verify(telegramNotificationService, times(1)).rentalNotification(rentalDto, "New rental added.");
    }

    @Test
    @DisplayName("addRental -> CarAlreadyRented")
    void addRental_CarAlreadyRented_ExceptionThrown() {
        RentalDto rentalDto = new RentalDto();
        rentalDto.setCarId(1L);

        Car car = new Car();
        car.setId(1L);

        when(carService.getCarById(1L)).thenReturn(car);
        when(rentalRepository.findByCar_IdAndActualReturnDateIsNull(1L)).thenReturn(Collections.singletonList(new Rental()));

        assertThrows(CarAlreadyRentedException.class, () -> rentalService.addRental(rentalDto));

        verify(carService, never()).updateCar(anyLong(), any());
        verify(telegramNotificationService, never()).rentalNotification(any(), any());
    }

    @Test
    @DisplayName("addRental -> InsufficientInventory")
    void addRental_InsufficientInventory_ExceptionThrown() {
        RentalDto rentalDto = new RentalDto();
        rentalDto.setCarId(1L);

        Car car = new Car();
        car.setId(1L);
        car.setInventory(0);

        when(carService.getCarById(1L)).thenReturn(car);

        assertThrows(InsufficientInventoryException.class, () -> rentalService.addRental(rentalDto));

        verify(carService, never()).updateCar(anyLong(), any());
        verify(telegramNotificationService, never()).rentalNotification(any(), any());
    }

    @Test
    @DisplayName("getRentalsByUserAndStatus -> ReturnsRentals")
    void getRentalsByUserAndStatus_ReturnsRentals() {
        Long userId = 1L;
        boolean carReturned = false;

        Rental rental = new Rental();
        rental.setId(1L);

        when(rentalRepository.findByUserIdAndActualReturnDateIsNull(userId, carReturned)).thenReturn(Collections.singletonList(rental));
        when(rentalMapper.toDtoList(Collections.singletonList(rental))).thenReturn(Collections.singletonList(new RentalDto()));

        List<RentalDto> result = rentalService.getRentalsByUserAndStatus(userId, carReturned);

        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("getRentalsByUserAndStatus -> ExceptionThrown")
    void getRentalsByUserAndStatus_NoRentalsFound_ExceptionThrown() {
        Long userId = 1L;
        boolean carReturned = false;

        when(rentalRepository.findByUserIdAndActualReturnDateIsNull(userId, carReturned)).thenReturn(Collections.emptyList());

        assertThrows(NoRentalsFoundException.class, () -> rentalService.getRentalsByUserAndStatus(userId, carReturned));
    }

    @Test
    @DisplayName("getRentalById -> ReturnsRental")
    void getRentalById_ValidId_ReturnsRental() {
        Long rentalId = 1L;

        Rental rental = new Rental();
        rental.setId(rentalId);

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(rentalMapper.toDto(rental)).thenReturn(new RentalDto());

        RentalDto result = rentalService.getRentalById(rentalId);

        assertNotNull(result);
    }

    @Test
    @DisplayName("getRentalById -> ExceptionThrown")
    void getRentalById_InvalidId_ExceptionThrown() {
        Long rentalId = 1L;

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        assertThrows(RentalNotFoundException.class, () -> rentalService.getRentalById(rentalId));
    }

    @Test
    @DisplayName("returnCar -> ReturnsCompleted")
    void returnCar_ValidRentalId_ReturnsCompletedRentalDto() {
        Long rentalId = 1L;

        Rental rental = new Rental();
        rental.setId(rentalId);
        rental.setCar(new Car());
        rental.getCar().setId(1L);

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(carService.getCarById(1L)).thenReturn(rental.getCar());
        when(rentalRepository.save(any())).thenReturn(rental);
        when(rentalMapper.toCompletedDto(rental)).thenReturn(new CompletedRentalDto());

        CompletedRentalDto result = rentalService.returnCar(rentalId);

        assertNotNull(result);
        assertNotNull(rental.getActualReturnDate());
    }

    @Test
    @DisplayName("returnCar -> Returned Exception")
    void returnCar_RentalAlreadyReturned_ExceptionThrown() {
        Long rentalId = 1L;

        Rental rental = new Rental();
        rental.setId(rentalId);
        rental.setActualReturnDate(LocalDate.now());

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        assertThrows(RentalReturnedException.class, () -> rentalService.returnCar(rentalId));
    }

    @Test
    @DisplayName("returnCar -> NotFound Exception")
    void returnCar_RentalNotFound_ExceptionThrown() {
        Long rentalId = 1L;

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        assertThrows(RentalNotFoundException.class, () -> rentalService.returnCar(rentalId));
    }

    @Test
    @DisplayName("checkOverdueRentals -> NoNotificationSent")
    void checkOverdueRentals_NoOverdueRentals_NoNotificationSent() {
        LocalDate today = LocalDate.now();

        when(rentalRepository.findByReturnDateBeforeAndActualReturnDateIsNull(today)).thenReturn(Collections.emptyList());

        rentalService.checkOverdueRentals();

        verify(telegramNotificationService, times(1)).sendNotification("No rentals overdue today!");
    }

    @Test
    @DisplayName("checkOverdueRentals -> NotificationSent")
    void checkOverdueRentals_OverdueRentals_NotificationSent() {
        LocalDate today = LocalDate.now();

        Rental overdueRental = new Rental();
        overdueRental.setId(1L);
        overdueRental.setReturnDate(today.minusDays(1));

        when(rentalRepository.findByReturnDateBeforeAndActualReturnDateIsNull(today)).thenReturn(Collections.singletonList(overdueRental));

        rentalService.checkOverdueRentals();

        verify(telegramNotificationService, times(1)).sendNotification("Overdue rental ID: 1. Return date was: " + overdueRental.getReturnDate());
    }
}

