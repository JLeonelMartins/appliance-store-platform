package com.martins.jonathan.appliancestore.sales.exception;

public class OutboxSerializationException extends RuntimeException {
    public OutboxSerializationException(Throwable cause) {
        super("The outbox event could not be serialized", cause);
    }
}
