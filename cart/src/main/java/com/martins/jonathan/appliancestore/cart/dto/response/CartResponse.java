package com.martins.jonathan.appliancestore.cart.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Long id,
        BigDecimal total,
        List<CartItemResponse> items
) {
}
