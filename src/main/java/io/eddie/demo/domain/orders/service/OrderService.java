package io.eddie.demo.domain.orders.service;

import io.eddie.demo.domain.orders.model.vo.CreateOrderRequest;
import io.eddie.demo.domain.orders.model.entity.Orders;
import io.eddie.demo.domain.orders.model.vo.OrderState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Orders createOrder(String accountCode, CreateOrderRequest request);

    Orders getOrder(String accountCode, String orderCode);

    Orders getOrderByCode(String code);

    Orders completeOrder(String accountCode, String orderCode);
    Orders cancelOrder(String accountCode, String orderCode);

    Page<Orders> getOrders(String accountCode, Pageable pageable);

}
