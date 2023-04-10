package com.jaelse.seebtc.resources.wallets.repository;

import com.jaelse.seebtc.resources.wallets.domain.WalletEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface WalletRepository extends ReactiveMongoRepository<WalletEntity, ObjectId>, WalletQueryRepository {

}
