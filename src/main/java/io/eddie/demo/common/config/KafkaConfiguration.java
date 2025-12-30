package io.eddie.demo.common.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfiguration {

    @Value("${custom.kafka.topic.account.event}")
    private String accountEventTopic;

    @Value("${custom.kafka.topic.cart.command}")
    private String cartCommandTopic;

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(
            ProducerFactory<String, Object> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic createAccountEventTopic() {
        return TopicBuilder.name(accountEventTopic)
                .build();
    }

    @Bean
    public NewTopic createCartCommandTopic() {
        return TopicBuilder.name(cartCommandTopic)
                .build();
    }

}
