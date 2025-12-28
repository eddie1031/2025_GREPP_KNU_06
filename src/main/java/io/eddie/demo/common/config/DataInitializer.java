package io.eddie.demo.common.config;

import io.eddie.demo.domain.accounts.model.dto.CreateAccountRequest;
import io.eddie.demo.domain.accounts.model.entity.Account;
import io.eddie.demo.domain.accounts.service.AccountService;
import io.eddie.demo.domain.carts.model.entity.Cart;
import io.eddie.demo.domain.carts.model.entity.CartItem;
import io.eddie.demo.domain.carts.model.vo.CreateCartItemRequest;
import io.eddie.demo.domain.carts.service.CartService;
import io.eddie.demo.domain.deposits.model.entity.Deposit;
import io.eddie.demo.domain.deposits.model.entity.DepositHistory;
import io.eddie.demo.domain.deposits.service.DepositService;
import io.eddie.demo.domain.orders.model.entity.Orders;
import io.eddie.demo.domain.orders.model.vo.CreateOrderRequest;
import io.eddie.demo.domain.orders.service.OrderService;
import io.eddie.demo.domain.payments.model.dto.PaymentRequest;
import io.eddie.demo.domain.payments.model.vo.PaymentType;
import io.eddie.demo.domain.payments.service.PaymentService;
import io.eddie.demo.domain.products.model.dto.UpsertProductRequest;
import io.eddie.demo.domain.products.model.entity.Product;
import io.eddie.demo.domain.products.service.ProductService;
import io.eddie.demo.domain.settlements.model.entity.Settlement;
import io.eddie.demo.domain.settlements.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.IntStream;

@Slf4j
@Component
@Profile("!prod")
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final AccountService accountService;
    private final ProductService productService;
    private final CartService cartService;

    private final DepositService depositService;
    private final OrderService orderService;
    private final PaymentService paymentService;

    @Override
    public void run(ApplicationArguments args) throws Exception {


        List<Cart> cartList = new ArrayList<>();

        List<Account> accountList = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> accountService.create(new CreateAccountRequest("user%d".formatted(i), "user%d".formatted(i), "user%d@user.com".formatted(i))))
                .toList();

        List<Product> productList = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> productService.save(
                        accountList.get(i % accountList.size()).getCode()
                        , new UpsertProductRequest("product%d".formatted(i), "product%d description".formatted(i), randomPrice())
                ))
                .toList();

        accountList.forEach(a ->
                depositService.charge(a.getCode()
                        , DepositHistory.DepositHistoryType.CHARGE_TRANSFER
                        , 100_000_000L)
        );

        Map<String, Orders> orderList = new HashMap<>();
        Map<String, List<String>> accountToCartItemCodes = new HashMap<>();

        // 주문 아이템 데이터 1만건
        IntStream.rangeClosed(1, 2000)
                .forEach(i -> {
                    Product targetProduct = productList.get(i % productList.size());

                    CreateCartItemRequest cartItemReq = new CreateCartItemRequest(
                            targetProduct.getCode(),
                            targetProduct.getName(),
                            targetProduct.getPrice(),
                            randomQuantity()
                    );

                    // 모든 회원에게 아이템 추가
                    accountList.forEach(a -> {
                        CartItem created = cartService.appendItem(a.getCode(), cartItemReq);
                        accountToCartItemCodes
                                .computeIfAbsent(a.getCode(), k -> new ArrayList<>())
                                .add(created.getCode());
                    });

                });

        accountList.forEach(a -> {
            List<String> itemCodes = accountToCartItemCodes.getOrDefault(a.getCode(), List.of());
            if (!itemCodes.isEmpty()) {
                Orders order = orderService.createOrder(a.getCode(), new CreateOrderRequest(itemCodes));
                orderList.put(a.getCode(), order);
            }
        });

        orderList.forEach((a, o) -> {
            paymentService.pay(new PaymentRequest(a, PaymentType.DEPOSIT, o.getCode(), o.getTotalPrice()));
        });

        /*
        생성된 정산 데이터 5만건
        List<Settlement> settlements = IntStream.rangeClosed(1, 50_000)
                .mapToObj(i -> {

                    Product targetProduct = productList.get(i % productList.size());

                    Settlement settlement = Settlement.builder()
                            .buyerCode(accountList.get(i % accountList.size()).getCode())
                            .sellerCode(targetProduct.getAccountCode())
                            .settlementRate(0.5)
                            .totalAmount(targetProduct.getPrice())
                            .build();

                    settlement.applySettlementPolicy();

                    return settlement;
                })
                .toList();

        settlementRepository.saveAll(settlements);
         */

        log.info("데이터 성공적으로 로딩 완료!");

    }

    private Long randomPrice() {
        return Math.abs(new Random().nextLong() % 1_000);
    }

    private int randomQuantity() {
        return (int) ((Math.random() + 1) * 10);
    }

}
