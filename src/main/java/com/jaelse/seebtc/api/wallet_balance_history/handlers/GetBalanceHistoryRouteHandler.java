package com.jaelse.seebtc.api.wallet_balance_history.handlers;

import com.jaelse.seebtc.lib.assemblers.WalletBalanceHistoryAssembler;
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

@Component
public class GetBalanceHistoryRouteHandler implements HandlerFunction<ServerResponse> {
    private final WalletBalanceHistoryService service;
    private final WalletBalanceHistoryAssembler assembler;

    @Autowired
    public GetBalanceHistoryRouteHandler(WalletBalanceHistoryService service,
                                         WalletBalanceHistoryAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return Mono.just(new ObjectId(request.pathVariable("id")))
                .flatMap(service::findById)
                .map(assembler::assemble)
                .flatMap(model -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(model)))
                .onErrorResume(throwable -> ServerResponse
                        .status(HttpStatus.BAD_REQUEST)
                        .body(BodyInserters.fromValue("Error while handling the request: " + throwable.getCause()))
                );
    }
}
