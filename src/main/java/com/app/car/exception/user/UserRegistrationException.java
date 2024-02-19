package com.app.car.exception.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserRegistrationException extends Exception {
    public UserRegistrationException(String message) {
        super(message);
    }

    public UserRegistrationException(ExceptionType type) {
        super(type.getMessage());
    }

    @AllArgsConstructor
    @Getter
    public enum ExceptionType {
        EMAIL_ALREADY_EXISTS("Can't register user with email!"),
        PASSWORDS_DO_NOT_MATCH("Passwords do not match!");

        private final String message;
    }
}
