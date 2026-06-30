package com.martins.jonathan.appliancestore.cart.product;

import com.martins.jonathan.appliancestore.cart.dto.response.CartResponse;
import com.martins.jonathan.appliancestore.cart.product.dto.ProductClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "product")
public interface IProductAPI {

    @GetMapping("/api/product/{id}")
    ProductClientResponse getProductById(@PathVariable("id") Long id);

    

}
