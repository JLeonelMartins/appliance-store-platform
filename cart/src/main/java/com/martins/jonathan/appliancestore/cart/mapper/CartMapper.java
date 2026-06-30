package com.martins.jonathan.appliancestore.cart.mapper;

import com.martins.jonathan.appliancestore.cart.dto.response.CartItemResponse;
import com.martins.jonathan.appliancestore.cart.dto.response.CartResponse;
import com.martins.jonathan.appliancestore.cart.model.Cart;
import com.martins.jonathan.appliancestore.cart.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    public CartItemResponse toItemResponse(CartItem item) {
        return new CartItemResponse(
                item.getId(),
                item.getProductId(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }

    public CartResponse toResponse(Cart cart) {

        List<CartItemResponse> items = cart.getItems()
                .stream()
                .map(this::toItemResponse)
                .toList();

        return new CartResponse(
                cart.getId(),
                cart.getTotal(),
                items
        );
    }


}
