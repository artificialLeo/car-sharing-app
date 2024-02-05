package com.app.car.notification;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.GetUpdates;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

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
        // Your logic for handling updates
        sendInitialMessage(update.getMessage().getChatId());
    }

    private void sendInitialMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Initial message: Hello from your bot!");

        try {
            execute(sendMessage);
            System.out.println("Initial message sent successfully to chat ID: " + chatId);
        } catch (Exception e) {
            System.err.println("Error sending initial message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onBotStart() {
        Update initialUpdate = new Update();
        onUpdateReceived(initialUpdate);
    }
}

