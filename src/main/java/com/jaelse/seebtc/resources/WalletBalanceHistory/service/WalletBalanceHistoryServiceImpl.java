package com.jaelse.seebtc.resources.WalletBalanceHistory.service;

import com.jaelse.seebtc.lib.dtos.CreateWalletBalanceDto;
import com.jaelse.seebtc.lib.dtos.UpdateWalletBalanceDto;
import com.jaelse.seebtc.lib.queries.WalletBalanceHistoryQuery;
import com.jaelse.seebtc.lib.queries.WalletQuery;
import com.jaelse.seebtc.resources.WalletBalanceHistory.domain.WalletBalanceHistoryEntity;
import com.jaelse.seebtc.resources.WalletBalanceHistory.repository.WalletBalanceHistoryRepository;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class WalletBalanceHistoryServiceImpl implements WalletBalanceHistoryService {

    private final WalletBalanceHistoryRepository repository;

    @Autowired
    public WalletBalanceHistoryServiceImpl(WalletBalanceHistoryRepository repository) {
        this.repository = repository;
    }


    @Override
    public Flux<WalletBalanceHistoryEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<WalletBalanceHistoryEntity> findById(ObjectId id) {
        return repository.findById(id);
    }

    @Override
    public Mono<WalletBalanceHistoryEntity> create(CreateWalletBalanceDto createWalletBalanceDto) {
        return repository.insert(WalletBalanceHistoryEntity.builder()
                .id(new ObjectId())
                .walletId(createWalletBalanceDto.getWalletId())
                .year(createWalletBalanceDto.getYear())
                .dayOfMonth(createWalletBalanceDto.getDayOfMonth())
                .hour(createWalletBalanceDto.getHour())
                .amount(new Decimal128(createWalletBalanceDto.getAmount()))
                .dateTime(createWalletBalanceDto.getDateTime())
                .build());
    }

    @Override
    public Mono<WalletBalanceHistoryEntity> update(ObjectId id, UpdateWalletBalanceDto updateWalletDto) {
        return repository.findById(id)
                .flatMap(walletBalanceHistoryEntity -> {
                    var amount = walletBalanceHistoryEntity.getAmount().bigDecimalValue().add(updateWalletDto.getAmount());

                    walletBalanceHistoryEntity.setAmount(new Decimal128(amount));
                    return repository.save(walletBalanceHistoryEntity);
                });
    }

    @Override
    public Flux<WalletBalanceHistoryEntity> find(WalletBalanceHistoryQuery query) {
        return repository.find(query);
    }

    @Override
    public Mono<WalletBalanceHistoryEntity> findByYearDayHour(ObjectId walletId, Integer year, Integer day, Integer hour) {
        return repository.findByYearDayHour(walletId, year, day, hour);
    }

    @Override
    public Mono<WalletBalanceHistoryEntity> findLastBalanceHistory(ObjectId id) {
        return repository.findLastBalanceHistory(id);
    }

    @Override
    public Flux<WalletBalanceHistoryEntity> findBetweenHours(ObjectId walletId, LocalDateTime startDatetime, LocalDateTime endDatetime) {
        return repository.findBetweenHours(walletId, startDatetime, endDatetime);
    }

    @Override
    public Mono<Long> count(WalletBalanceHistoryQuery query) {
        return repository.count(query);
    }
}
