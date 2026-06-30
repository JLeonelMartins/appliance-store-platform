package com.martins.jonathan.appliancestore.cart.exception;

public class ProductServiceUnavailableException extends RuntimeException {
    public ProductServiceUnavailableException() {
        super("Product service is temporarily unavailable");
    }
}
