package com.app.car.exception;

public class RentalReturnedException extends RuntimeException {

    public RentalReturnedException(String message) {
        super(message);
    }

    public RentalReturnedException(String message, Throwable cause) {
        super(message, cause);
    }
}

