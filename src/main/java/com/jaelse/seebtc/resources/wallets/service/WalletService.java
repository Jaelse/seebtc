package com.jaelse.seebtc.resources.wallets.service;

import com.jaelse.seebtc.lib.dtos.CreateWalletDto;
import com.jaelse.seebtc.lib.dtos.UpdateWalletDto;
import com.jaelse.seebtc.lib.queries.WalletQuery;
import com.jaelse.seebtc.resources.wallets.domain.WalletEntity;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WalletService {

    Flux<WalletEntity> getAllWallets();

    Mono<WalletEntity> getWalletById(ObjectId id);

    Mono<WalletEntity> createWallet(CreateWalletDto wallet);

    Mono<WalletEntity> updateWallet(ObjectId id, UpdateWalletDto updateWalletDto);

    Mono<WalletEntity> deleteWalletById(ObjectId id);

    Flux<WalletEntity> find(WalletQuery query);

    Mono<Long> count(WalletQuery query);
}
