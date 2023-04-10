package com.jaelse.seebtc.resources.wallets.repository;

import com.jaelse.seebtc.lib.queries.WalletQuery;
import com.jaelse.seebtc.resources.wallets.domain.WalletEntity;
import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class WalletQueryRepositoryImpl implements WalletQueryRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    public WalletQueryRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    public Flux<WalletEntity> getAllWallets() {
        return reactiveMongoTemplate.findAll(WalletEntity.class);
    }

    public Mono<WalletEntity> getWalletById(ObjectId id) {
        return reactiveMongoTemplate.findById(id, WalletEntity.class);
    }

    @Override
    public Flux<WalletEntity> find(WalletQuery query) {
        return reactiveMongoTemplate.find(make(query), WalletEntity.class);
    }

    @Override
    public Mono<Long> count(WalletQuery query) {
        return reactiveMongoTemplate.count(make(query), WalletEntity.class);
    }

    private Query make(WalletQuery query) {
        Query result = new Query().with(
                Sort.by(Sort.Direction.DESC, "id")
        );

        if (!query.getIds().isEmpty()) {
            result.addCriteria(Criteria.where("id").in(query.getIds()));
        }

        return result;
    }
}
