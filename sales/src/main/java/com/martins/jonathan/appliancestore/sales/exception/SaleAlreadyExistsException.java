package com.martins.jonathan.appliancestore.sales.exception;

public class SaleAlreadyExistsException extends RuntimeException {

    public SaleAlreadyExistsException(Long cartId) {
        super("A sale already exists for cart with id: " + cartId);
    }
}
