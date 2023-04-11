package com.jaelse.seebtc.resources.wallets.service;

import com.jaelse.seebtc.lib.dtos.CreateWalletDto;
import com.jaelse.seebtc.lib.dtos.UpdateWalletDto;
import com.jaelse.seebtc.lib.queries.WalletQuery;
import com.jaelse.seebtc.resources.wallets.domain.WalletEntity;
import com.jaelse.seebtc.resources.wallets.repository.WalletRepository;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository repository;

    @Autowired
    public WalletServiceImpl(WalletRepository repository) {
        this.repository = repository;
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
                .balance(new Decimal128(new BigDecimal(dto.initialBalance())))
                .isDeleted(false)
                .build());
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
