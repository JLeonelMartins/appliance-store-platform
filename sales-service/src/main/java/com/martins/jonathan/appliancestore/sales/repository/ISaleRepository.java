package com.martins.jonathan.appliancestore.sales.repository;

import com.martins.jonathan.appliancestore.sales.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISaleRepository extends JpaRepository<Sale, Long> {

    boolean existsByCartId(Long cartId);

}
