package com.jaelse.seebtc.resources.WalletBalanceHistory.handlers;

import com.jaelse.seebtc.lib.dtos.CreateWalletBalanceDto;
import com.jaelse.seebtc.lib.dtos.UpdateWalletBalanceDto;
import com.jaelse.seebtc.lib.events.EventHandler;
import com.jaelse.seebtc.lib.events.TransactionCreatedEvent;
import com.jaelse.seebtc.resources.WalletBalanceHistory.service.WalletBalanceHistoryService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TransactionCreatedEventHandler implements EventHandler<TransactionCreatedEvent> {
    private final WalletBalanceHistoryService service;
    private final Logger log = LoggerFactory.getLogger(TransactionCreatedEventHandler.class);

    public TransactionCreatedEventHandler(WalletBalanceHistoryService service) {
        this.service = service;
    }

    @Override
    public Mono<Void> handle(ObjectId key, TransactionCreatedEvent evt) {
        var payload = evt.payload();

        return service.findByYearDayHour(
                        key,
                        payload.getDateTime().getYear(),
                        payload.getDateTime().getDayOfMonth(),
                        payload.getDateTime().getHour())
                .flatMap(history -> {
                    log.info("Found current wallet balance history. Updating balance....");
                    return service.update(history.getId(), UpdateWalletBalanceDto.builder().amount(payload.getAmount()).build());
                })
                .switchIfEmpty(
                        service.findLastBalanceHistory(key)
                                .filter(history -> history.getDateTime().isBefore(payload.getDateTime()))
                                .flatMap(lastHistory -> {
                                    var newAmount = lastHistory.getAmount().bigDecimalValue().add(payload.getAmount());
                                    log.info("No wallet balance history found for this hour. Balance {}. Creating...", newAmount);
                                    return service.create(CreateWalletBalanceDto.builder()
                                            .walletId(key)
                                            .year(payload.getDateTime().getYear())
                                            .dayOfMonth(payload.getDateTime().getDayOfMonth())
                                            .hour(payload.getDateTime().getHour())
                                            .amount(newAmount)
                                            .dateTime(payload.getDateTime())
                                            .build());
                                })
                )
                .then();
    }
}
