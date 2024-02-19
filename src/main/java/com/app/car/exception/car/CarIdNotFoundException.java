package com.app.car.exception.car;

public class CarIdNotFoundException extends RuntimeException {

    public CarIdNotFoundException(Long id) {
        super("Car entity not found, with id: " + id);
    }
}
