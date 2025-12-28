package io.eddie.demo.domain.orders.service;

import io.eddie.demo.domain.carts.model.entity.Cart;
import io.eddie.demo.domain.carts.model.entity.CartItem;
import io.eddie.demo.domain.carts.service.CartService;
import io.eddie.demo.domain.orders.model.entity.OrderItem;
import io.eddie.demo.domain.orders.model.entity.Orders;
import io.eddie.demo.domain.orders.model.vo.CreateOrderRequest;
import io.eddie.demo.domain.orders.model.vo.OrderState;
import io.eddie.demo.domain.orders.repository.OrderItemRepository;
import io.eddie.demo.domain.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final CartService cartService;

    @Override
    @Transactional
    public Orders createOrder(String accountCode, CreateOrderRequest request) {

        Orders order = Orders.builder()
                .accountCode(accountCode)
                .build();

        List<CartItem> targetItems = cartService.getItemsByCodes(request.cartItemCodes());

        if ( targetItems.isEmpty() )
            throw new IllegalStateException("선택된 아이템이 없습니다");

        List<OrderItem> orderItems = targetItems.stream()
                .map(i -> {
                    OrderItem item = OrderItem.builder()
                            .productCode(i.getProductCode())
                            .productName(i.getProductName())
                            .productPrice(i.getProductPrice())
                            .quantity(i.getQuantity())
                            .build();

                    order.addItem(item);
                    return item;
                })
                .toList();

        orderRepository.save(order);

        orderItemRepository.saveAll(orderItems);
        cartService.deleteItemsByCodes(request.cartItemCodes());

        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public Orders getOrder(String accountCode, String orderCode) {
        return orderRepository.findByAccountCodeAndCode(accountCode, orderCode)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
    }

    @Override
    public Orders getOrderByCode(String code) {
        return orderRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public Orders completeOrder(String accountCode, String orderCode) {

        Orders targetOrder = getOrder(accountCode, orderCode);
        targetOrder.complete();

        return targetOrder;
    }

    @Override
    @Transactional
    public Orders cancelOrder(String accountCode, String orderCode) {

        Orders order = getOrder(accountCode, orderCode);

        if ( order.getOrderState() == OrderState.CANCELLED ) {
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }

//        Optional<Payment> paymentOptional = paymentApplication.findPaymentByOrderCode(code);
//
//        if (paymentOptional.isEmpty()) {
//            order.cancel();
//            return new OrderCodeResponse(code);
//        }
//
//        Payment payment = paymentOptional.get();
//
//        // 결제가 완료된 상태라면
//        if ( payment.getPaymentStatus() == Payment.PaymentStatus.PAYMENT_COMPLETED ) {
//
//            // 결제일자로부터 3일 이전 까지는 취소 가능
//            if ( payment.getCreatedAt().plusDays(3).isAfter(LocalDateTime.now())) {
//                paymentApplication.refundToDeposit(new PaymentRequest(code, calcTotalPrice(getOrderItemDescriptions(order))));
//                order.cancel();
//                return new OrderCodeResponse(code);
//            }
//
//        }
//
//        if ( payment.getPaymentStatus() == Payment.PaymentStatus.PAYMENT_READY ) {
//            // 결제가 아직 발생하지 않았다면
//            order.cancel();
//            return new OrderCodeResponse(code);
//        }
//
//        throw new InvalidOrderRequestException("취소할 수 없는 상태의 주문입니다.");

        return order;

    }

    @Override
    @Transactional(readOnly = true)
    public Page<Orders> getOrders(String accountCode, Pageable pageable) {
        return orderRepository.findAllByAccountCode(accountCode, pageable);
    }

}

