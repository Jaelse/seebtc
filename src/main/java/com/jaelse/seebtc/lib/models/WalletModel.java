package com.jaelse.seebtc.lib.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class WalletModel {

    private String id;
    private BigDecimal balance;
}
