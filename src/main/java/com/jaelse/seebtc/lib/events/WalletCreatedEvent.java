package com.jaelse.seebtc.lib.events;

import lombok.*;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.Optional;

public record WalletCreatedEvent(
        WalletCreatedEvent.Payload payload) implements BaseEvent<WalletCreatedEvent.Payload> {

    @Override
    public Optional<ObjectId> key() {
        return Optional.of(payload.id);
    }

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class Payload {
        private ObjectId id;
        private BigDecimal amount;
    }
}
