package io.eddie.demo.domain.orders.repository;

import io.eddie.demo.domain.orders.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
