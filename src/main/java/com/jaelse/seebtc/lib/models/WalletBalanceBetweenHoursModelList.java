package com.jaelse.seebtc.lib.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Builder
public class WalletBalanceBetweenHoursModelList {
    private List<Balance> history;

    @Setter
    @Getter
    @Builder
    public static final class Balance {
        private final String datetime;
        private final BigDecimal amount;
    }
}
