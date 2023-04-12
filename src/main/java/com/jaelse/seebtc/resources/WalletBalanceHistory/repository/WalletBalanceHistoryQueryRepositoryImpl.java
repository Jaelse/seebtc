package com.jaelse.seebtc.resources.WalletBalanceHistory.repository;

import com.jaelse.seebtc.lib.queries.WalletBalanceHistoryQuery;
import com.jaelse.seebtc.resources.WalletBalanceHistory.domain.WalletBalanceHistoryEntity;
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
public class WalletBalanceHistoryQueryRepositoryImpl implements WalletBalanceHistoryQueryRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    public WalletBalanceHistoryQueryRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Flux<WalletBalanceHistoryEntity> find(WalletBalanceHistoryQuery query) {
        return reactiveMongoTemplate.find(make(query), WalletBalanceHistoryEntity.class);
    }

    @Override
    public Mono<WalletBalanceHistoryEntity> findByYearDayHour(ObjectId walletId, Integer year, Integer day, Integer hour) {
        return reactiveMongoTemplate.
                findOne(makeYearDateHour(walletId, year, day, hour), WalletBalanceHistoryEntity.class);
    }

    @Override
    public Mono<WalletBalanceHistoryEntity> findLastBalanceHistory(ObjectId walletId) {
        Query result = new Query().with(
                Sort.by(Sort.Direction.DESC, "id")
        );

        return reactiveMongoTemplate.findOne(result, WalletBalanceHistoryEntity.class);
    }

    @Override
    public Flux<WalletBalanceHistoryEntity> findBetweenHours(ObjectId walletId, LocalDateTime startDatetime, LocalDateTime endDatetime) {
        Query result = new Query();
        result.addCriteria(Criteria.where("walletId")
                        .is(walletId))
                .addCriteria(Criteria.where("dateTime")
                        .lte(endDatetime)
                        .gte(startDatetime));

        return reactiveMongoTemplate.find(result, WalletBalanceHistoryEntity.class);
    }

    @Override
    public Mono<Long> count(WalletBalanceHistoryQuery query) {
        return reactiveMongoTemplate.count(make(query), WalletBalanceHistoryEntity.class);
    }

    private Query makeYearDateHour(ObjectId id, Integer year, Integer day, Integer hour) {
        Query result = new Query();
        result.addCriteria(Criteria.where("walletId").is(id));
        result.addCriteria(Criteria.where("year").is(year));
        result.addCriteria(Criteria.where("dayOfMonth").is(day));
        result.addCriteria(Criteria.where("hour").is(hour));

        return result;
    }

    private Query make(WalletBalanceHistoryQuery query) {
        Query result = new Query().with(
                Sort.by(Sort.Direction.DESC, "id")
        );

        if (!query.getIds().isEmpty()) {
            result.addCriteria(Criteria.where("id").in(query.getIds()));
        }

        if (!query.getWalletIds().isEmpty()) {
            result.addCriteria(Criteria.where("walletId").in(query.getIds()));
        }

        if (!query.getYears().isEmpty()) {
            result.addCriteria(Criteria.where("year").in(query.getIds()));
        }

        if (!query.getDays().isEmpty()) {

            result.addCriteria(Criteria.where("dayOfMonth").in(query.getIds()));
        }

        if (!query.getHours().isEmpty()) {
            result.addCriteria(Criteria.where("hour").in(query.getIds()));
        }

        result.skip(query.getOffset()).limit(query.getLimit());
        return result;
    }
}
