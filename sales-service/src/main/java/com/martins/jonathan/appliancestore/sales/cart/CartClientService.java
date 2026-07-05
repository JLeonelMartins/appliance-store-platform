package com.martins.jonathan.appliancestore.sales.cart;

import com.martins.jonathan.appliancestore.sales.cart.dto.CartClientResponse;
import com.martins.jonathan.appliancestore.sales.exception.CartNotFoundException;
import com.martins.jonathan.appliancestore.sales.exception.CartServiceUnavailableException;
import feign.FeignException;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class CartClientService {

    private final ICartAPI cartAPI;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;
    private final Retry retry;

    public CartClientService(
            ICartAPI cartAPI,
            CircuitBreakerFactory<?, ?> circuitBreakerFactory,
            RetryRegistry retryRegistry
    ) {
        this.cartAPI = cartAPI;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.retry = retryRegistry.retry("cartServiceRetry");
    }

    public CartClientResponse getCartById(Long cartId) {

        Supplier<CartClientResponse> cartCall = () -> callCart(cartId);

        Supplier<CartClientResponse> retryableCall = Retry.decorateSupplier(retry, cartCall);

        return circuitBreakerFactory
                .create("cartService")
                .run(
                        retryableCall,
                        this::fallback
                );
    }

    private CartClientResponse callCart(Long cartId) {

        try {
            return cartAPI.getCartById(cartId);

        } catch (FeignException.NotFound exception) {
            throw new CartNotFoundException(cartId);
        }
    }

    private CartClientResponse fallback(Throwable throwable) {

        CartNotFoundException cartNotFoundException = findCause(throwable, CartNotFoundException.class);

        if (cartNotFoundException != null) {
            throw cartNotFoundException;
        }

        throw new CartServiceUnavailableException();
    }

    private <T extends Throwable> T findCause(Throwable throwable, Class<T> exceptionType) {

        Throwable currentException = throwable;

        while (currentException != null) {

            if (exceptionType.isInstance(currentException)) {
                return exceptionType.cast(currentException);
            }

            currentException = currentException.getCause();
        }

        return null;
    }
}