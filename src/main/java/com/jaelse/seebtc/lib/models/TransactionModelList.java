package com.jaelse.seebtc.lib.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TransactionModelList {
    private Long offset;
    private Integer limit;
    private Long count;
    private List<TransactionModel> transactions;
}
