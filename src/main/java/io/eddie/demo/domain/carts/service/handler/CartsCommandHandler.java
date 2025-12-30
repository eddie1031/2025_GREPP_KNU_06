package io.eddie.demo.domain.carts.service.handler;

import io.eddie.demo.common.model.command.CreateCartCommand;
import lombok.RequiredArgsConstructor;
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

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaHandler
    public void handleCommand(@Payload CreateCartCommand createCartCommand) {

    }

}
