package com.app.car.service;

import com.app.car.dto.rental.RentalDto;
import com.app.car.exception.notification.TelegramExecutionException;
import com.app.car.model.Car;
import com.app.car.model.User;
import com.app.car.notification.NotificationTelegramBot;
import com.app.car.repository.CarRepository;
import com.app.car.repository.UserRepository;
import com.app.car.service.impl.TelegramNotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Telegram Notification Service Tests")
public class TelegramNotificationServiceImplTest {

    @Mock
    private NotificationTelegramBot notificationTelegramBot;

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TelegramNotificationServiceImpl telegramNotificationService;

    private RentalDto rentalDto;
    private SendMessage sendMessageCarNotFound;
    private SendMessage sendMessageUserNotFound;
    private SendMessage sendMessageTelegramException;
    private Car car;
    private User user;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        rentalDto = RentalDto.builder()
                .id(1L)
                .rentalDate(LocalDate.of(2022, 2, 10))
                .returnDate(LocalDate.of(2022, 2, 20))
                .carId(1L)
                .userId(2L)
                .build();

        sendMessageCarNotFound = SendMessage.builder().chatId(1L).text("Car not found").build();
        sendMessageUserNotFound = SendMessage.builder().chatId(1L).text("User not found").build();
        sendMessageTelegramException = SendMessage.builder().chatId(1L).text("Error executing Telegram API operation").build();

        car = Car.builder().id(1L).model("TestModel").build();
        user = User.builder().id(1L).email("TestUser").build();
    }

    @Test
    @DisplayName("sendNotification success")
    void sendNotification_Success() throws TelegramApiException {
        when(notificationTelegramBot.execute(any(SendMessage.class))).thenReturn(null);

        assertDoesNotThrow(() -> telegramNotificationService.sendNotification("Test message"));

        verify(notificationTelegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("sendNotification -> TelegramExecutionException")
    void sendNotification_TelegramApiException() throws TelegramApiException {
        when(notificationTelegramBot.execute(any(SendMessage.class))).thenThrow(new TelegramApiException("Test Exception"));

        assertThrows(TelegramExecutionException.class, () -> telegramNotificationService.sendNotification("Test message"));

        verify(notificationTelegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("rentalNotification success")
    void rentalNotification_Success() throws TelegramApiException {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        when(notificationTelegramBot.execute(any(SendMessage.class))).thenReturn(null);

        assertDoesNotThrow(() -> telegramNotificationService.rentalNotification(rentalDto, "Test message"));

        verify(notificationTelegramBot, times(1)).execute(any(SendMessage.class));
    }

}
