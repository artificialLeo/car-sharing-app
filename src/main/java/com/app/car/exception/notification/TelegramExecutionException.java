package com.app.car.exception.notification;

public class TelegramExecutionException extends RuntimeException {

    public TelegramExecutionException(Throwable cause) {
        super("Error executing Telegram API operation ", cause);
    }
}

