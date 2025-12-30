package io.eddie.demo.domain.accounts.saga;

import io.eddie.demo.common.model.command.CreateCartCommand;
import io.eddie.demo.common.model.event.AccountCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@KafkaListener(
        topics = {
                "${custom.kafka.topic.account.event}"
        }
)
@RequiredArgsConstructor
public class AccountSaga {

    @Value("${custom.kafka.topic.cart.command}")
    private String cartCommandTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaHandler
    public void handleEvent(@Payload AccountCreatedEvent event) {

        CreateCartCommand createCartCommand = new CreateCartCommand(event.accountCode());
        kafkaTemplate.send(cartCommandTopic, createCartCommand);

    }

}
