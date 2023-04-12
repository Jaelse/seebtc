package com.jaelse.seebtc.lib.events;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public record TransactionCreatedEvent(
        TransactionCreatedEvent.Payload payload) implements BaseEvent<TransactionCreatedEvent.Payload> {

    @Override
    public Optional<ObjectId> key() {
        return Optional.of(payload.walletId);
    }

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class Payload {
        private ObjectId id;
        private ObjectId walletId;
        private BigDecimal amount;
        private LocalDateTime dateTime;
    }
}
