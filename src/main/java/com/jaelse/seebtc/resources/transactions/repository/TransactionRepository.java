package com.jaelse.seebtc.resources.transactions.repository;

import com.jaelse.seebtc.resources.transactions.domain.TransactionEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TransactionRepository extends ReactiveMongoRepository<TransactionEntity, ObjectId>, TransactionQueryRepository {
}
