package com.jaelse.seebtc.resources.WalletBalanceHistory.repository;

import com.jaelse.seebtc.lib.queries.WalletBalanceHistoryQuery;
import com.jaelse.seebtc.resources.WalletBalanceHistory.domain.WalletBalanceHistoryEntity;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface WalletBalanceHistoryQueryRepository {
    Flux<WalletBalanceHistoryEntity> find(WalletBalanceHistoryQuery query);

    Mono<WalletBalanceHistoryEntity> findByYearDayHour(ObjectId walletId, Integer year, Integer day, Integer hour);

    Mono<WalletBalanceHistoryEntity> findLastBalanceHistory(ObjectId walletId);

    Flux<WalletBalanceHistoryEntity> findBetweenHours(ObjectId walletId, LocalDateTime startDatetime, LocalDateTime endDatetime);

    Mono<Long> count(WalletBalanceHistoryQuery query);
}
