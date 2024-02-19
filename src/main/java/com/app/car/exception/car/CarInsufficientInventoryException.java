package com.app.car.exception.car;

public class CarInsufficientInventoryException extends RuntimeException {

    public CarInsufficientInventoryException(String message) {
        super(message);
    }

    public CarInsufficientInventoryException(Long carId) {
        super("Insufficient inventory for car with id : " + carId);
    }
}

