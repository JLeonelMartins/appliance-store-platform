package com.martins.jonathan.appliancestore.sales.cart;

import com.martins.jonathan.appliancestore.sales.cart.dto.CartClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart")
public interface ICartAPI {

    @GetMapping("/api/cart/{id}")
    CartClientResponse getCartById(
            @PathVariable("id") Long id
    );
}
