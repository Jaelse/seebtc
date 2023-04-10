package com.jaelse.seebtc.lib.models;

import com.jaelse.seebtc.resources.wallets.domain.WalletEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class WalletModelList {

    private final Long offset;
    private final Integer limit;
    private final Long count;
    private final List<WalletModel> wallets;
}
