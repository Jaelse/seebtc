package com.jaelse.seebtc.resources.wallets.domain;

import lombok.*;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@AllArgsConstructor
@Document("wallets")
public class WalletEntity {

    @Id
    @NonNull
    private final ObjectId id;

    @NonNull
    private final Decimal128 balance;

    @NonNull
    @Builder.Default
    private Boolean isDeleted = false;
}
