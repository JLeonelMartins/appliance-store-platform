package com.martins.jonathan.appliancestore.cart.exception;

public class CartNotFoundException extends RuntimeException {


    public CartNotFoundException(Long id) {
        super("Cart not found with id: " + id);
    }

    
}
