package com.martins.jonathan.appliancestore.product.service;

import com.martins.jonathan.appliancestore.product.dto.ProductRequest;
import com.martins.jonathan.appliancestore.product.dto.ProductResponse;
import com.martins.jonathan.appliancestore.product.model.Product;
import com.martins.jonathan.appliancestore.product.repository.IProductRepository;

import java.util.List;

public interface IProductService  {

    List<ProductResponse> findAll();

    ProductResponse findById(Long id);

    ProductResponse createProduct(ProductRequest reques);

    void deleteById(Long id);

    ProductResponse update(Long id, ProductRequest request);

}
