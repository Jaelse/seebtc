package com.jaelse.seebtc.api.wallet_balance_history.handlers;

import com.jaelse.seebtc.lib.assemblers.WalletBalanceHistoryAssembler;
import com.jaelse.seebtc.lib.models.WalletBalanceHistoryModelList;
import com.jaelse.seebtc.resources.WalletBalanceHistory.service.WalletBalanceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ListBalanceHistoryRouteHandler implements HandlerFunction<ServerResponse> {

    private final WalletBalanceHistoryService service;
    private final WalletBalanceHistoryAssembler assembler;

    @Autowired
    public ListBalanceHistoryRouteHandler(WalletBalanceHistoryService service,
                                          WalletBalanceHistoryAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }


    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return Mono.just(assembler.assemble(request.queryParams()))
                .flatMap(query -> service.find(query)
                        .collectList()
                        .map(assembler::assemble)
                        .zipWith(service.count(query))
                        .map(listNCount -> WalletBalanceHistoryModelList.builder()
                                .limit(query.getLimit())
                                .offset(query.getOffset())
                                .history(listNCount.getT1())
                                .count(listNCount.getT2()))
                ).flatMap(model -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(model))
                );
    }
}
