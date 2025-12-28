package io.eddie.demo.domain.carts.repository;

import io.eddie.demo.domain.carts.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByAccountCode(String accountCode);

}
