package com.jaelse.seebtc.resources.transactions.service;

import com.jaelse.seebtc.lib.dtos.CreateTransactionDto;
import com.jaelse.seebtc.lib.queries.TransactionQuery;
import com.jaelse.seebtc.resources.transactions.domain.TransactionEntity;
import com.jaelse.seebtc.resources.transactions.repository.TransactionRepository;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository repository;

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<TransactionEntity> getAll() {
        return repository.getAllTransactions();
    }

    @Override
    public Mono<TransactionEntity> findById(ObjectId id) {
        return repository.findById(id);
    }

    @Override
    public Mono<TransactionEntity> create(ObjectId walletId, CreateTransactionDto transactionDto) {
        return repository.insert(TransactionEntity.builder()
                .id(new ObjectId())
                .walletId(walletId)
                .amount(new Decimal128(new BigDecimal(transactionDto.getAmount())))
                .dateTime(getDateTime(transactionDto.getDatetime()))
                .build());
    }

    @Override
    public Flux<TransactionEntity> find(TransactionQuery query) {
        return repository.find(query);
    }

    @Override
    public Mono<Long> count(TransactionQuery query) {
        return repository.count(query);
    }

    private LocalDateTime getDateTime(String datetime) {
        var zonedDateTime = ZonedDateTime.parse(datetime);
        System.out.println(zonedDateTime);
        return LocalDateTime.ofInstant(zonedDateTime.toInstant(), ZoneOffset.UTC);
    }

}
