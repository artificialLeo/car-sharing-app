package com.app.car.exception.rental;

public class NoRentalsFoundException extends RuntimeException {

    public NoRentalsFoundException(Long userId, boolean carReturned) {
        super("No rentals found for user with id: " + userId + " and carReturned: " + carReturned);
    }
}

