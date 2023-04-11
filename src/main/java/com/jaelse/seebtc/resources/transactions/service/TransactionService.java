package com.jaelse.seebtc.resources.transactions.service;

import com.jaelse.seebtc.lib.dtos.CreateTransactionDto;
import com.jaelse.seebtc.lib.queries.TransactionQuery;
import com.jaelse.seebtc.resources.transactions.domain.TransactionEntity;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Flux<TransactionEntity> getAll();

    Mono<TransactionEntity> findById(ObjectId id);

    Mono<TransactionEntity> create(ObjectId walletId, CreateTransactionDto transactionDto);

    Flux<TransactionEntity> find(TransactionQuery query);

    Mono<Long> count(TransactionQuery query);
}
