package com.martins.jonathan.appliancestore.cart.service;

import com.martins.jonathan.appliancestore.cart.dto.request.CartItemRequest;
import com.martins.jonathan.appliancestore.cart.dto.response.CartResponse;

import java.util.List;

public interface ICartService {

    CartResponse createCart();

    CartResponse findById(Long id);

    List<CartResponse> findAll();

    CartResponse addItem(Long cartId, CartItemRequest request);

    CartResponse removeItem(Long cartId, Long productId);
}
