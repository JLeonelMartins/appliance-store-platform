package com.martins.jonathan.appliancestore.cart.exception;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(Long cartId, Long productId) {
        super("Product with id " + productId + " was not found in cart with id " + cartId);
    }
}
