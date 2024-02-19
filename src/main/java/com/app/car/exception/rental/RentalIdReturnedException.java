package com.app.car.exception.rental;

public class RentalIdReturnedException extends RuntimeException {

    public RentalIdReturnedException(Long rentalId) {
        super("Rental with id " + rentalId + " has already been returned.");
    }

}

