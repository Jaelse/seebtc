package com.jaelse.seebtc.resources.WalletBalanceHistory.repository;

import com.jaelse.seebtc.resources.WalletBalanceHistory.domain.WalletBalanceHistoryEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface WalletBalanceHistoryRepository extends ReactiveMongoRepository<WalletBalanceHistoryEntity, ObjectId>, WalletBalanceHistoryQueryRepository {
}
