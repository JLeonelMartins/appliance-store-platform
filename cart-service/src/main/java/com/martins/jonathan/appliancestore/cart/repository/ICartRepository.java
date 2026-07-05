package com.martins.jonathan.appliancestore.cart.repository;

import com.martins.jonathan.appliancestore.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {
}
