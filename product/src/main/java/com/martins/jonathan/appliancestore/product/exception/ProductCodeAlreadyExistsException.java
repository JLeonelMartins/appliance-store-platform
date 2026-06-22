package com.martins.jonathan.appliancestore.product.exception;

public class ProductCodeAlreadyExistsException extends RuntimeException {

    public ProductCodeAlreadyExistsException(String code) {
        super("A product with code " + code + " already exists");
    }
}
