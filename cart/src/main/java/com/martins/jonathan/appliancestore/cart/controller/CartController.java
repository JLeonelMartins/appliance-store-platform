package com.martins.jonathan.appliancestore.cart.controller;

import com.martins.jonathan.appliancestore.cart.dto.request.CartItemRequest;
import com.martins.jonathan.appliancestore.cart.dto.response.CartResponse;
import com.martins.jonathan.appliancestore.cart.service.ICartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(   name = "Carts",
        description = "Operations for managing shopping carts")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    @Operation(
            summary = "Create a cart",
            description = "Creates a new empty shopping cart")
    @ApiResponse(
            responseCode = "201",
            description = "Cart created successfully")
    @PostMapping
    public ResponseEntity<CartResponse> createCart() {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.createCart());
    }

    @Operation(
            summary = "Get cart by ID",
            description = "Returns a shopping cart with all its items"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cart found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cart not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.findById(id));
    }

    @Operation(
            summary = "Get all carts",
            description = "Returns all registered shopping carts"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Carts retrieved successfully"
    )
    @GetMapping
    public ResponseEntity<List<CartResponse>> findAll() {
        return ResponseEntity.ok(cartService.findAll());
    }

    @Operation(
            summary = "Add product to cart",
            description = "Gets product information from the product service and adds it to the cart"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product added successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cart or product not found"
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Product service unavailable"
            )
    })
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartResponse> addItem(@PathVariable Long cartId, @Valid @RequestBody CartItemRequest request) {

        CartResponse response = cartService.addItem(cartId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Remove product from cart",
            description = "Removes a product from the cart and recalculates the total"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product removed successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cart or cart item not found"
            )
    })
    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable Long cartId, @PathVariable Long productId) {

        CartResponse response = cartService.removeItem(cartId, productId);
        return ResponseEntity.ok(response);
    }

}
