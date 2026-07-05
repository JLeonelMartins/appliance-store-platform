package com.martins.jonathan.appliancestore.sales.mapper;

import com.martins.jonathan.appliancestore.sales.dto.response.SaleResponse;
import com.martins.jonathan.appliancestore.sales.model.Sale;
import org.springframework.stereotype.Component;

@Component
public class SaleMapper {

    public SaleResponse toResponse(Sale sale) {

        return new SaleResponse(
                sale.getId(),
                sale.getCartId(),
                sale.getSaleDate(),
                sale.getTotal()
        );
    }
}
