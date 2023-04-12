package com.jaelse.seebtc.lib.queries;

import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
public class WalletBalanceHistoryQuery {
    @Builder.Default
    private final Long offset = 0L;

    @Builder.Default
    private final Integer limit = 30;

    @Builder.Default
    private final Set<ObjectId> ids = new HashSet<>();

    @Builder.Default
    private final Set<ObjectId> walletIds =new HashSet<>();

    @Builder.Default
    private final Set<Integer> years = new HashSet<>();

    @Builder.Default
    private final Set<Integer> days = new HashSet<>();

    @Builder.Default
    private final Set<Integer> hours = new HashSet<>();
}
