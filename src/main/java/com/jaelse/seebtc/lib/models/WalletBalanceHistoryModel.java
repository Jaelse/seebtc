package com.jaelse.seebtc.lib.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class WalletBalanceHistoryModel {
    private String id;
    private String walletId;
    private Integer year;
    private Integer dayOfMonth;
    private Integer hour;
    private BigDecimal amount;
    private String dateTime;
}
