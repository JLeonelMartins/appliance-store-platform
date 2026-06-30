package com.martins.jonathan.appliancestore.sales.service;

import com.martins.jonathan.appliancestore.sales.dto.request.SaleRequest;
import com.martins.jonathan.appliancestore.sales.dto.response.SaleResponse;

import java.util.List;

public interface ISaleService {

    SaleResponse createSale(SaleRequest request);

    SaleResponse findById(Long id);

    List<SaleResponse> findAll();

}