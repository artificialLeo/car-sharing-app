package com.app.car.exception;

public class CarAlreadyRentedException extends RuntimeException {

    public CarAlreadyRentedException(String message) {
        super(message);
    }
}

