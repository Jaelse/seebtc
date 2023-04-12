package com.jaelse.seebtc.lib.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.Decimal128;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class UpdateWalletBalanceDto {

    @NonNull
    private final BigDecimal amount;
}
