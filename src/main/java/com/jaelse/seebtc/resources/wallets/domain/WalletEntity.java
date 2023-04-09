package com.jaelse.seebtc.resources.wallets.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Setter
@Getter
@Document("wallets")
public class WalletEntity {

    @Id
    private final ObjectId id;

    @NonNull
    private final Decimal128 balance;
}
