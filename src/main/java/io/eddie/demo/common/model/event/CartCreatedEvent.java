package io.eddie.demo.common.model.event;

public record CartCreatedEvent(
        String accountCode,
        String cartCode
) {
}
