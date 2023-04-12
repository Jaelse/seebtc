package com.jaelse.seebtc.api.wallet_balance_history.handlers;

import com.jaelse.seebtc.lib.assemblers.WalletBalanceHistoryAssembler;
import com.jaelse.seebtc.lib.dtos.BalanceHistoryBetweenHoursDto;
import com.jaelse.seebtc.lib.models.WalletBalanceHistoryModelList;
import com.jaelse.seebtc.lib.queries.WalletBalanceHistoryQuery;
import com.jaelse.seebtc.resources.WalletBalanceHistory.service.WalletBalanceHistoryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Component
public class WalletHistoryBetweenHoursRouteHandler implements HandlerFunction<ServerResponse> {

    private final WalletBalanceHistoryService service;
    private final WalletBalanceHistoryAssembler assembler;

    @Autowired
    public WalletHistoryBetweenHoursRouteHandler(WalletBalanceHistoryService service,
                                                 WalletBalanceHistoryAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.bodyToMono(BalanceHistoryBetweenHoursDto.class)
                .zipWith(Mono.just(new ObjectId(request.pathVariable("walletId"))))
                .flatMap(tuple -> service.findBetweenHours(tuple.getT2(),
                                getDateTime(tuple.getT1().getStartDatetime()),
                                getDateTime(tuple.getT1().getEndDatetime()))
                        .collectList()
                        .map(assembler::assembleBalance))
                .flatMap(model -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(model)))
                .onErrorResume(throwable -> ServerResponse
                        .status(HttpStatus.BAD_REQUEST)
                        .body(BodyInserters.fromValue("Error while handling the request: " + throwable.getCause()))
                );
    }

    private LocalDateTime getDateTime(String datetime) {
        var zonedDateTime = ZonedDateTime.parse(datetime);
        return LocalDateTime.ofInstant(zonedDateTime.toInstant(), ZoneOffset.UTC);
    }

}
