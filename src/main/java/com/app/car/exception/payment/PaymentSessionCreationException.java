package com.app.car.exception.payment;

public class PaymentSessionCreationException extends RuntimeException {

    public PaymentSessionCreationException(Throwable cause) {
        super("Error creating payment session ", cause);
    }
}
