package com.app.car.exception.car;

public class CarIdAlreadyRentedException extends RuntimeException {

    public CarIdAlreadyRentedException(Long carId) {
        super("Car with ID " + carId + " is already rented and not returned yet.");
    }

}

