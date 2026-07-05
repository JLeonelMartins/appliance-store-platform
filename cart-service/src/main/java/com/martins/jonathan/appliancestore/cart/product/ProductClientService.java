package com.martins.jonathan.appliancestore.cart.product;

import com.martins.jonathan.appliancestore.cart.exception.ProductNotFoundException;
import com.martins.jonathan.appliancestore.cart.exception.ProductServiceUnavailableException;
import com.martins.jonathan.appliancestore.cart.product.dto.ProductClientResponse;
import feign.FeignException;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class ProductClientService {

    private final IProductAPI productAPI;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;
    private final Retry retry;

    public ProductClientService(
            IProductAPI productAPI,
            CircuitBreakerFactory<?, ?> circuitBreakerFactory,
            RetryRegistry retryRegistry
    ) {
        this.productAPI = productAPI;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.retry = retryRegistry.retry("productServiceRetry");
    }

    public ProductClientResponse getProductById(Long productId) {

        Supplier<ProductClientResponse> productCall =
                () -> callProduct(productId);

        Supplier<ProductClientResponse> retryableCall =
                Retry.decorateSupplier(retry, productCall);

        return circuitBreakerFactory
                .create("productService")
                .run(
                        retryableCall,
                        this::fallback
                );
    }

    private ProductClientResponse callProduct(Long productId) {

        try {
            return productAPI.getProductById(productId);

        } catch (FeignException.NotFound exception) {
            throw new ProductNotFoundException(productId);
        }
    }

    private <T extends Throwable> T findCause(
            Throwable throwable,
            Class<T> exceptionType
    ) {

        Throwable currentException = throwable;

        while (currentException != null) {

            if (exceptionType.isInstance(currentException)) {
                return exceptionType.cast(currentException);
            }

            currentException = currentException.getCause();
        }

        return null;
    }

    private ProductClientResponse fallback(Throwable throwable) {

        ProductNotFoundException notFoundException =
                findCause(
                        throwable,
                        ProductNotFoundException.class
                );

        if (notFoundException != null) {
            throw notFoundException;
        }

        throw new ProductServiceUnavailableException();
    }
}