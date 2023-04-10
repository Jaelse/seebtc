package com.jaelse.seebtc.lib.queries;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class WalletQuery {

    @Builder.Default
    private final Long offset = 0L;

    @Builder.Default
    private final Integer limit = 30;

    @Builder.Default
    private final Set<ObjectId> ids = new HashSet<>();
}
