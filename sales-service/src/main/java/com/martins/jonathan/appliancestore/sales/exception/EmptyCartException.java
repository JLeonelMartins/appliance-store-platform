package com.martins.jonathan.appliancestore.sales.exception;

public class EmptyCartException extends RuntimeException {

    public EmptyCartException(Long cartId) {
        super("Cart with id " + cartId + " cannot be sold because it is empty");
    }
}
