package com.app.car.exception;

public class NoRentalsFoundException extends RuntimeException {

    public NoRentalsFoundException(String message) {
        super(message);
    }
}

