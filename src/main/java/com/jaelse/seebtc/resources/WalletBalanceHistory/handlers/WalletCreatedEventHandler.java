package com.jaelse.seebtc.resources.WalletBalanceHistory.handlers;

import com.jaelse.seebtc.lib.dtos.CreateWalletBalanceDto;
import com.jaelse.seebtc.lib.events.EventHandler;
import com.jaelse.seebtc.lib.events.WalletCreatedEvent;
import com.jaelse.seebtc.resources.WalletBalanceHistory.service.WalletBalanceHistoryService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class WalletCreatedEventHandler implements EventHandler<WalletCreatedEvent> {

    private final WalletBalanceHistoryService service;
    private final Logger logger = LoggerFactory.getLogger(WalletCreatedEventHandler.class);

    public WalletCreatedEventHandler(WalletBalanceHistoryService service) {
        this.service = service;
    }

    @Override
    public Mono<Void> handle(ObjectId key, WalletCreatedEvent evt) {
        var payload = evt.payload();
        var now = LocalDateTime.now(ZoneOffset.UTC);
        return service.create(CreateWalletBalanceDto.builder()
                        .walletId(key)
                        .year(now.getYear())
                        .dayOfMonth(now.getDayOfMonth())
                        .hour(now.getHour())
                        .amount(payload.getAmount())
                        .dateTime(now)
                        .build())
                .doOnNext(balanceHistory -> logger.info("Balance History for wallet id {} has been created...", balanceHistory.getWalletId()))
                .then();
    }
}
