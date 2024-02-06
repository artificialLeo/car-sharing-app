package com.app.car.service;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface TelegramNotificationService {
    void sendNotification(String message) throws TelegramApiException;
}
