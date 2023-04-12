package com.jaelse.seebtc.resources.WalletBalanceHistory.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@Document("wallet_balance_history")
public class WalletBalanceHistoryEntity {

    @Id
    @NonNull
    private ObjectId id;

    @NonNull
    private ObjectId walletId;

    @NonNull
    private Integer year;

    @NonNull
    private Integer dayOfMonth;

    @NonNull
    private Integer hour;

    @NonNull
    private Decimal128 amount;

    @NonNull
    private LocalDateTime dateTime;
}
