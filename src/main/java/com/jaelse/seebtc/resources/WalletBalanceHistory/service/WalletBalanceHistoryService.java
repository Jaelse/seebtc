package com.jaelse.seebtc.resources.WalletBalanceHistory.service;

import com.jaelse.seebtc.lib.dtos.CreateWalletBalanceDto;
import com.jaelse.seebtc.lib.dtos.CreateWalletDto;
import com.jaelse.seebtc.lib.dtos.UpdateWalletBalanceDto;
import com.jaelse.seebtc.lib.dtos.UpdateWalletDto;
import com.jaelse.seebtc.lib.queries.WalletBalanceHistoryQuery;
import com.jaelse.seebtc.lib.queries.WalletQuery;
import com.jaelse.seebtc.resources.WalletBalanceHistory.domain.WalletBalanceHistoryEntity;
import com.jaelse.seebtc.resources.wallets.domain.WalletEntity;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface WalletBalanceHistoryService {
    Flux<WalletBalanceHistoryEntity> getAll();

    Mono<WalletBalanceHistoryEntity> findById(ObjectId id);

    Mono<WalletBalanceHistoryEntity> create(CreateWalletBalanceDto wallet);

    Mono<WalletBalanceHistoryEntity> update(ObjectId id, UpdateWalletBalanceDto updateWalletDto);

    Flux<WalletBalanceHistoryEntity> find(WalletBalanceHistoryQuery query);

    Mono<WalletBalanceHistoryEntity> findByYearDayHour(ObjectId id, Integer year, Integer day, Integer hour);

    Mono<WalletBalanceHistoryEntity> findLastBalanceHistory(ObjectId id);

    Flux<WalletBalanceHistoryEntity> findBetweenHours(ObjectId walletId, LocalDateTime startDatetime, LocalDateTime endDatetime);

    Mono<Long> count(WalletBalanceHistoryQuery query);
}
