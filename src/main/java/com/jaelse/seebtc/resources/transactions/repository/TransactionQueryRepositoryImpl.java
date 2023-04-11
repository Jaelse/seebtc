package com.jaelse.seebtc.resources.transactions.repository;

import com.jaelse.seebtc.lib.queries.TransactionQuery;
import com.jaelse.seebtc.lib.queries.WalletQuery;
import com.jaelse.seebtc.resources.transactions.domain.TransactionEntity;
import com.jaelse.seebtc.resources.wallets.domain.WalletEntity;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public class TransactionQueryRepositoryImpl implements TransactionQueryRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    public TransactionQueryRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Flux<TransactionEntity> getAllTransactions() {
        return reactiveMongoTemplate.findAll(TransactionEntity.class);
    }

    @Override
    public Mono<TransactionEntity> getTransactionById(ObjectId id) {
        return reactiveMongoTemplate.findById(id, TransactionEntity.class);
    }

    @Override
    public Flux<TransactionEntity> find(TransactionQuery query) {
        return reactiveMongoTemplate.find(make(query), TransactionEntity.class);
    }

    @Override
    public Mono<Long> count(TransactionQuery query) {
        return reactiveMongoTemplate.count(make(query), TransactionEntity.class);
    }


    private Query make(TransactionQuery query) {
        Query result = new Query();

        result.with(Sort.by(Sort.Direction.DESC, "id"));

        if (!query.getIds().isEmpty()) {
            result.addCriteria(Criteria.where("id").in(query.getIds()));
        }

        if (!query.getWalletIds().isEmpty()) {
            result.addCriteria(Criteria.where("walletId").in(query.getWalletIds()));
        }


        result.addCriteria(Criteria.where("dateTime")
                .lte(query.getEndTime())
                .gte(query.getStartTime())
        );

        result.skip(query.getOffset()).limit(query.getLimit());

        return result;
    }
}
