package com.martins.jonathan.appliancestore.sales.controller;

import com.martins.jonathan.appliancestore.sales.dto.request.SaleRequest;
import com.martins.jonathan.appliancestore.sales.dto.response.SaleResponse;
import com.martins.jonathan.appliancestore.sales.service.ISaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Sales", description = "Operations for registering and consulting sales")
@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final ISaleService saleService;

    public SaleController(ISaleService saleService) {
        this.saleService = saleService;
    }

    @Operation(
            summary = "Create a sale",
            description = """
                    Creates a sale from an existing cart.
                    The cart must exist, contain products and must not have been sold previously.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Sale created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or empty cart"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cart not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "A sale already exists for the cart"
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Cart service is temporarily unavailable"
            )
    })
    @PostMapping
    public ResponseEntity<SaleResponse> createSale(@Valid @RequestBody SaleRequest request) {

        SaleResponse response = saleService.createSale(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Get sale by ID",
            description = "Returns a sale using its unique identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sale found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sale not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.findById(id));
    }

    @Operation(
            summary = "Get all sales",
            description = "Returns every registered sale"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Sales retrieved successfully"
    )
    @GetMapping
    public ResponseEntity<List<SaleResponse>> findAll() {
        return ResponseEntity.ok(saleService.findAll());
    }
}
