package com.martins.jonathan.appliancestore.notification.exception;

public class EmailDeliveryException extends RuntimeException {

    public EmailDeliveryException(String recipient, Throwable cause) {
        super("Email could not be delivered to: " + recipient, cause);
    }
}