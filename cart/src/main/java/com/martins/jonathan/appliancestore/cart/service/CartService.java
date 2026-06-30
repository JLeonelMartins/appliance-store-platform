package com.martins.jonathan.appliancestore.cart.service;

import com.martins.jonathan.appliancestore.cart.dto.request.CartItemRequest;
import com.martins.jonathan.appliancestore.cart.dto.response.CartResponse;
import com.martins.jonathan.appliancestore.cart.exception.CartItemNotFoundException;
import com.martins.jonathan.appliancestore.cart.exception.CartNotFoundException;
import com.martins.jonathan.appliancestore.cart.exception.ProductNotFoundException;
import com.martins.jonathan.appliancestore.cart.mapper.CartMapper;
import com.martins.jonathan.appliancestore.cart.model.Cart;
import com.martins.jonathan.appliancestore.cart.model.CartItem;
import com.martins.jonathan.appliancestore.cart.product.IProductAPI;
import com.martins.jonathan.appliancestore.cart.product.ProductClientService;
import com.martins.jonathan.appliancestore.cart.product.dto.ProductClientResponse;
import com.martins.jonathan.appliancestore.cart.repository.ICartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService implements ICartService {

    private final ICartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductClientService productClientService;

    public CartService(ICartRepository cartRepository, CartMapper cartMapper, ProductClientService productClientService) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.productClientService = productClientService;
    }

    @Override
    public CartResponse createCart() {

        Cart cart = new Cart();
        cart.setTotal(BigDecimal.ZERO);

        Cart savedCart = cartRepository.save(cart);

        return cartMapper.toResponse(savedCart);
    }

    @Override
    public CartResponse findById(Long id) {

        Cart cart = cartRepository.findById(id).orElseThrow(() -> new CartNotFoundException(id));

        return cartMapper.toResponse(cart);
    }

    @Override
    public List<CartResponse> findAll() {
        return cartRepository.findAll()
                .stream()
                .map(cartMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CartResponse addItem(Long cartId, CartItemRequest request) {

        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId));

        ProductClientResponse product;

        try {
            product = productClientService.getProductById(request.productId());
        } catch (feign.FeignException.NotFound exception) {
            throw new ProductNotFoundException(request.productId());
        }

        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item ->
                        item.getProductId().equals(request.productId())
                )
                .findFirst()
                .orElse(null);

        if (cartItem == null) {

            cartItem = new CartItem();

            cartItem.setProductId(product.id());
            cartItem.setQuantity(request.quantity());
            cartItem.setUnitPrice(product.price());
            cartItem.setSubtotal(
                    product.price().multiply(
                            BigDecimal.valueOf(request.quantity())
                    )
            );
            cartItem.setCart(cart);

            cart.getItems().add(cartItem);

        } else {

            int newQuantity = cartItem.getQuantity() + request.quantity();

            cartItem.setQuantity(newQuantity);
            cartItem.setUnitPrice(product.price());
            cartItem.setSubtotal(
                    product.price().multiply(
                            BigDecimal.valueOf(newQuantity)
                    )
            );
        }

        recalculateTotal(cart);

        Cart savedCart = cartRepository.save(cart);

        return cartMapper.toResponse(savedCart);
    }

    private void recalculateTotal(Cart cart) {

        BigDecimal total = cart.getItems()
                .stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotal(total);
    }

    @Override
    @Transactional
    public CartResponse removeItem(Long cartId, Long productId) {

        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId));

        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException(cartId, productId));

        cart.getItems().remove(cartItem);

        recalculateTotal(cart);

        Cart savedCart = cartRepository.save(cart);

        return cartMapper.toResponse(savedCart);
    }


}
