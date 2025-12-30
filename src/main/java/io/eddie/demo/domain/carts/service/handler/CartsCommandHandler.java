package io.eddie.demo.domain.carts.service.handler;

import io.eddie.demo.common.model.command.CreateCartCommand;
import io.eddie.demo.common.model.event.CartCreatedEvent;
import io.eddie.demo.domain.carts.model.entity.Cart;
import io.eddie.demo.domain.carts.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(
        topics = {
                "${custom.kafka.topic.cart.command}"
        }
)
@RequiredArgsConstructor
public class CartsCommandHandler {

    @Value("${custom.kafka.topic.cart.event}")
    private String cartEventTopic;

    private final CartService cartService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaHandler
    public void handleCommand(@Payload CreateCartCommand createCartCommand) {

        Cart cart = cartService.save(createCartCommand.accountCode());
//        account.setCartCode(cart.getCode());
        CartCreatedEvent cartCreatedEvent = new CartCreatedEvent(
                createCartCommand.accountCode(),
                cart.getCode()
        );

        kafkaTemplate.send(cartEventTopic, cartCreatedEvent);

    }

}
