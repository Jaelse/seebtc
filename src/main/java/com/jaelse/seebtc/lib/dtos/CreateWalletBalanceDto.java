package com.jaelse.seebtc.lib.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class CreateWalletBalanceDto {

    @NonNull
    private final ObjectId walletId;

    @NonNull
    private final Integer year;

    @NonNull
    private final Integer dayOfMonth;

    @NonNull
    private final Integer hour;

    @NonNull
    private final BigDecimal amount;

    @NonNull
    private final LocalDateTime dateTime;
}
