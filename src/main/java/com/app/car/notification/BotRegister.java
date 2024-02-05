package com.app.car.notification;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotRegister {
    private final NotificationTelegramBot notificationTelegramBot;

    public BotRegister(NotificationTelegramBot notificationTelegramBot) {
        this.notificationTelegramBot = notificationTelegramBot;
    }

    @PostConstruct
    public void registerBot() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(notificationTelegramBot);
    }
}

