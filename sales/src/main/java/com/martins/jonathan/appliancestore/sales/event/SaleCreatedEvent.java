package com.martins.jonathan.appliancestore.sales.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleCreatedEvent(
        Long saleId,
        Long cartId,
        BigDecimal total,
        LocalDateTime saleDate,
        String customerEmail
) {
}