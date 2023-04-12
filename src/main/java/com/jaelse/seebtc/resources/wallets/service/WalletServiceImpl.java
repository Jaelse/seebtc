package com.jaelse.seebtc.resources.wallets.service;

import com.jaelse.seebtc.lib.dtos.CreateWalletDto;
import com.jaelse.seebtc.lib.dtos.UpdateWalletDto;
import com.jaelse.seebtc.lib.events.EventEmitter;
import com.jaelse.seebtc.lib.events.Topic;
import com.jaelse.seebtc.lib.events.TransactionCreatedEvent;
import com.jaelse.seebtc.lib.events.WalletCreatedEvent;
import com.jaelse.seebtc.lib.queries.WalletQuery;
import com.jaelse.seebtc.resources.wallets.domain.WalletEntity;
import com.jaelse.seebtc.resources.wallets.repository.WalletRepository;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository repository;
    private final EventEmitter eventEmitter;
    private final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    @Autowired
    public WalletServiceImpl(WalletRepository repository, EventEmitter eventEmitter) {
        this.repository = repository;
        this.eventEmitter = eventEmitter;
    }

    @Override
    public Flux<WalletEntity> getAll() {
        return repository.getAllWallets();
    }

    @Override
    public Mono<WalletEntity> findById(ObjectId id) {
        return repository.findById(id);
    }

    @Override
    public Mono<WalletEntity> create(CreateWalletDto dto) {
        return repository.insert(WalletEntity.builder()
                        .id(new ObjectId())
                        .balance(new Decimal128(new BigDecimal(dto.getInitialBalance())))
                        .isDeleted(false)
                        .build())
                .flatMap(wallet -> eventEmitter.emit(Topic.WALLET_CREATED,
                                new WalletCreatedEvent(WalletCreatedEvent.Payload.builder()
                                        .id(wallet.getId())
                                        .amount(wallet.getBalance().bigDecimalValue())
                                        .build()))
                        .doOnSuccess(record -> logger.info("Sent {} offset: {}", WalletCreatedEvent.class, record.recordMetadata().offset()))
                        .map(x -> wallet));
    }

    @Override
    public Mono<WalletEntity> update(ObjectId id, UpdateWalletDto dto) {
        //TODO: and fields that is could update
        return repository.findById(id)
                .flatMap(repository::save);
    }

    @Override
    public Mono<WalletEntity> delete(ObjectId id) {
        return repository.findById(id)
                .flatMap(wallet -> {
                    wallet.setIsDeleted(true);
                    return repository.save(wallet);
                });
    }

    @Override
    public Flux<WalletEntity> find(WalletQuery query) {
        return repository.find(query);
    }

    @Override
    public Mono<Long> count(WalletQuery query) {
        return repository.count(query);
    }
}
