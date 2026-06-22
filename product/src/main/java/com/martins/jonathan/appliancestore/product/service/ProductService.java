package com.martins.jonathan.appliancestore.product.service;

import com.martins.jonathan.appliancestore.product.dto.ProductRequest;
import com.martins.jonathan.appliancestore.product.dto.ProductResponse;
import com.martins.jonathan.appliancestore.product.exception.ProductCodeAlreadyExistsException;
import com.martins.jonathan.appliancestore.product.exception.ProductNotFoundException;
import com.martins.jonathan.appliancestore.product.mapper.ProductMapper;
import com.martins.jonathan.appliancestore.product.model.Product;
import com.martins.jonathan.appliancestore.product.repository.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {

    private final IProductRepository productRepository;
    private final ProductMapper productMapper;


    public ProductService(IProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(id)
                );
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        if (productRepository.existsByCode(request.code())) {
            throw new ProductCodeAlreadyExistsException(request.code());
        }

        Product product = productMapper.toEntity(request);

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);

    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(id)
                );

        if (productRepository.existsByCodeAndIdNot(request.code(), id)) {
            throw new ProductCodeAlreadyExistsException(request.code());
        }

        productMapper.updateEntity(existingProduct, request);

        Product updatedProduct = productRepository.save(existingProduct);

        return productMapper.toResponse(updatedProduct);
    }
}
