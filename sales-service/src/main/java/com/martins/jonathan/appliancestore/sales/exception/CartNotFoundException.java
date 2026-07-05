package com.martins.jonathan.appliancestore.sales.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Long cartId) {
      super("Cart not found with id: " + cartId);
    }
}
