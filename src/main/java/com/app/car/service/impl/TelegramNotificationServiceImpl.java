package com.app.car.service.impl;

import com.app.car.dto.rental.RentalDto;
import com.app.car.exception.car.CarIdNotFoundException;
import com.app.car.exception.telegramNotification.TelegramExecutionException;
import com.app.car.exception.user.UserNotFoundException;
import com.app.car.notification.NotificationTelegramBot;
import com.app.car.repository.CarRepository;
import com.app.car.repository.UserRepository;
import com.app.car.service.TelegramNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class TelegramNotificationServiceImpl implements TelegramNotificationService {
    private final NotificationTelegramBot notificationTelegramBot;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    @Override
    public void sendNotification(String message) {
        final String botChat = "348586971";
        SendMessage sendMessage = new SendMessage(botChat, message);

        try {
            notificationTelegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new TelegramExecutionException(e);
        }
    }

    public void rentalNotification(RentalDto rental, String message) {
        StringBuilder fullMessage = new StringBuilder(message);
        fullMessage.append("\nRental Details:\n");
        fullMessage.append("ID: ").append(rental.getId()).append("\n");
        fullMessage.append("Rental Date: ").append(rental.getRentalDate()).append("\n");
        fullMessage.append("Return Date: ").append(rental.getReturnDate()).append("\n");

        String carModel = carRepository
                .findById(rental.getCarId())
                .orElseThrow(()
                        -> new CarIdNotFoundException(rental.getCarId()))
                .getModel();
        String userName = userRepository
                .findById(rental.getUserId())
                .orElseThrow(()
                        -> new UserNotFoundException(rental.getUserId()))
                .getUsername();
        fullMessage.append("Car model: ").append(carModel).append("\n");
        fullMessage.append("Your username: ").append(userName).append("\n");
        final String botChat = "348586971";
        SendMessage sendMessage = new SendMessage(botChat, fullMessage.toString());

        try {
            notificationTelegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new TelegramExecutionException(e);
        }
    }

}
