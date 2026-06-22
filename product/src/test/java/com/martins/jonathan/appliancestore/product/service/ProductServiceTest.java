package com.martins.jonathan.appliancestore.product.service;

import com.martins.jonathan.appliancestore.product.dto.ProductRequest;
import com.martins.jonathan.appliancestore.product.dto.ProductResponse;
import com.martins.jonathan.appliancestore.product.mapper.ProductMapper;
import com.martins.jonathan.appliancestore.product.model.Product;
import com.martins.jonathan.appliancestore.product.repository.IProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldCreateProductSuccessfully() {

        // Arrange
        ProductRequest request = new ProductRequest(
                "TV-001",
                "Smart TV 55",
                "Samsung",
                new BigDecimal("850000.00")
        );

        Product product = new Product();
        product.setCode("TV-001");
        product.setName("Smart TV 55");
        product.setBrand("Samsung");
        product.setPrice(new BigDecimal("850000.00"));

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setCode("TV-001");
        savedProduct.setName("Smart TV 55");
        savedProduct.setBrand("Samsung");
        savedProduct.setPrice(new BigDecimal("850000.00"));

        ProductResponse expectedResponse = new ProductResponse(
                1L,
                "TV-001",
                "Smart TV 55",
                "Samsung",
                new BigDecimal("850000.00")
        );

        when(productRepository.existsByCode("TV-001"))
                .thenReturn(false);

        when(productMapper.toEntity(request))
                .thenReturn(product);

        when(productRepository.save(product))
                .thenReturn(savedProduct);

        when(productMapper.toResponse(savedProduct))
                .thenReturn(expectedResponse);

        // Act
        ProductResponse result = productService.createProduct(request);

        // Assert
        assertEquals(expectedResponse, result);

        verify(productRepository).existsByCode("TV-001");
        verify(productRepository).save(product);
        verify(productMapper).toResponse(savedProduct);
    }

}
