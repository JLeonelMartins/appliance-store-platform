package com.martins.jonathan.appliancestore.sales.exception;

public class CartServiceUnavailableException extends RuntimeException {
    public CartServiceUnavailableException() {
      super("Cart service is temporarily unavailable");
    }
}
