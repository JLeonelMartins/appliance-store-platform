package com.martins.jonathan.appliancestore.product.mapper;

import com.martins.jonathan.appliancestore.product.dto.ProductRequest;
import com.martins.jonathan.appliancestore.product.dto.ProductResponse;
import com.martins.jonathan.appliancestore.product.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {

        Product product = new Product();

        product.setCode(request.code());
        product.setName(request.name());
        product.setBrand(request.brand());
        product.setPrice(request.price());

        return product;
    }

    public ProductResponse toResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getBrand(),
                product.getPrice()
        );
    }

    public void updateEntity(Product product, ProductRequest request) {
        product.setCode(request.code());
        product.setName(request.name());
        product.setBrand(request.brand());
        product.setPrice(request.price());
    }


}
