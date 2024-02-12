package com.app.car.exception.rental;

public class RentalIdNotFoundException extends RuntimeException {

    public RentalIdNotFoundException(Long id) {
        super("Rental entity not found, with id: " + id);
    }
}

