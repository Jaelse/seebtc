package com.jaelse.seebtc.resources.transactions.service;

import com.jaelse.seebtc.lib.dtos.CreateTransactionDto;
import com.jaelse.seebtc.lib.events.EventEmitter;
import com.jaelse.seebtc.lib.events.Topic;
import com.jaelse.seebtc.lib.events.TransactionCreatedEvent;
import com.jaelse.seebtc.lib.queries.TransactionQuery;
import com.jaelse.seebtc.resources.transactions.domain.TransactionEntity;
import com.jaelse.seebtc.resources.transactions.repository.TransactionRepository;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final EventEmitter eventEmitter;
    private final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    public TransactionServiceImpl(TransactionRepository repository,
                                  EventEmitter eventEmitter) {
        this.repository = repository;
        this.eventEmitter = eventEmitter;
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
                        .build())
                .flatMap(transactionEntity -> eventEmitter.emit(
                                Topic.TRANSACTION_CREATED,
                                new TransactionCreatedEvent(TransactionCreatedEvent.Payload.builder()
                                        .id(transactionEntity.getId())
                                        .walletId(transactionEntity.getWalletId())
                                        .amount(transactionEntity.getAmount().bigDecimalValue())
                                        .dateTime(transactionEntity.getDateTime())
                                        .build()))
                        .doOnSuccess(record -> logger.info("Sent {} offset: {}", TransactionCreatedEvent.class, record.recordMetadata().offset()))
                        .map(x -> transactionEntity)
                );
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
        return LocalDateTime.ofInstant(zonedDateTime.toInstant(), ZoneOffset.UTC);
    }

}
