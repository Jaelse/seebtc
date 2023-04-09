package com.jaelse.seebtc.resources.transactions.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@Document("transactions")
public class TransactionEntity {

    @NonNull
    private final ObjectId walletId;

    @NonNull
    private final LocalDateTime dateTime;

    @NonNull
    private final Decimal128 amount;
}
