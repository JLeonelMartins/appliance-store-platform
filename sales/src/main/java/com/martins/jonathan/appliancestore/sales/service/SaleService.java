package com.martins.jonathan.appliancestore.sales.service;

import com.martins.jonathan.appliancestore.sales.cart.CartClientService;
import com.martins.jonathan.appliancestore.sales.cart.dto.CartClientResponse;
import com.martins.jonathan.appliancestore.sales.dto.request.SaleRequest;
import com.martins.jonathan.appliancestore.sales.dto.response.SaleResponse;
import com.martins.jonathan.appliancestore.sales.event.SaleCreatedEvent;
import com.martins.jonathan.appliancestore.sales.exception.EmptyCartException;
import com.martins.jonathan.appliancestore.sales.exception.SaleAlreadyExistsException;
import com.martins.jonathan.appliancestore.sales.exception.SaleNotFoundException;
import com.martins.jonathan.appliancestore.sales.mapper.SaleMapper;
import com.martins.jonathan.appliancestore.sales.model.Sale;
import com.martins.jonathan.appliancestore.sales.outbox.service.OutboxEventService;
import com.martins.jonathan.appliancestore.sales.repository.ISaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleService implements ISaleService {

    private final ISaleRepository saleRepository;
    private final SaleMapper saleMapper;
    private final CartClientService cartClientService;
    private final OutboxEventService outboxEventService;


    public SaleService(
            ISaleRepository saleRepository,
            SaleMapper saleMapper,
            CartClientService cartClientService,
            OutboxEventService outboxEventService
    ) {
        this.saleRepository = saleRepository;
        this.saleMapper = saleMapper;
        this.cartClientService = cartClientService;
        this.outboxEventService = outboxEventService;
    }

    @Override
    @Transactional
    public SaleResponse createSale(SaleRequest request) {

        if (saleRepository.existsByCartId(request.cartId())) {
            throw new SaleAlreadyExistsException(request.cartId());
        }

        CartClientResponse cart = cartClientService.getCartById(request.cartId());

        if (cart.total() == null || cart.total().compareTo(BigDecimal.ZERO) <= 0) {

            throw new EmptyCartException(request.cartId());
        }

        Sale sale = new Sale();

        sale.setCartId(cart.id());
        sale.setSaleDate(LocalDateTime.now());
        sale.setTotal(cart.total());

        Sale savedSale = saleRepository.save(sale);

        SaleCreatedEvent event = new SaleCreatedEvent(
                        savedSale.getId(),
                        savedSale.getCartId(),
                        savedSale.getTotal(),
                        savedSale.getSaleDate()
        );

        outboxEventService.save(event);

        return saleMapper.toResponse(savedSale);
    }

    @Override
    public SaleResponse findById(Long id) {

        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException(id));

        return saleMapper.toResponse(sale);
    }

    @Override
    public List<SaleResponse> findAll() {

        return saleRepository.findAll()
                .stream()
                .map(saleMapper::toResponse)
                .toList();
    }
}
