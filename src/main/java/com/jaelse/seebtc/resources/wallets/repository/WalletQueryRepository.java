package com.jaelse.seebtc.resources.wallets.repository;

import com.jaelse.seebtc.lib.queries.WalletQuery;
import com.jaelse.seebtc.resources.wallets.domain.WalletEntity;
import org.apache.kafka.clients.admin.DeletedRecords;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WalletQueryRepository {
    Flux<WalletEntity> getAllWallets();

    Mono<WalletEntity> getWalletById(ObjectId id);

    Flux<WalletEntity> find(WalletQuery query);

    Mono<Long> count(WalletQuery query);
}
