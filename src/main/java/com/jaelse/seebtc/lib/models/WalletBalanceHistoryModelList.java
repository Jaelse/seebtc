package com.jaelse.seebtc.lib.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class WalletBalanceHistoryModelList {

    private Long offset;
    private Integer limit;
    private Long count;
    private List<WalletBalanceHistoryModel> history;

}
