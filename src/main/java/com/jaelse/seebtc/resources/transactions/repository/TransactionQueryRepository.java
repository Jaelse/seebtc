package com.jaelse.seebtc.resources.transactions.repository;

import com.jaelse.seebtc.lib.queries.TransactionQuery;
import com.jaelse.seebtc.lib.queries.WalletQuery;
import com.jaelse.seebtc.resources.transactions.domain.TransactionEntity;
import com.jaelse.seebtc.resources.wallets.domain.WalletEntity;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface TransactionQueryRepository {

    Flux<TransactionEntity> getAllTransactions();

    Mono<TransactionEntity> getTransactionById(ObjectId id);

    Flux<TransactionEntity> find(TransactionQuery query);

    Mono<Long> count(TransactionQuery query);
}
