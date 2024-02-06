package com.app.car.notification;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class NotificationTelegramBot extends TelegramLongPollingBot {
    public NotificationTelegramBot() {
        super("6834773387:AAFMV_Mt0liD48Xbgtsuog2SfLEiBckPQyU");
    }

    @Override
    public String getBotUsername() {
        return "rentNotification0001Bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        update.getMessage().getText();
    }
}
