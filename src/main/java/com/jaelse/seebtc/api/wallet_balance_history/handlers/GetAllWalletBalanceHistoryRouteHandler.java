package com.jaelse.seebtc.api.wallet_balance_history.handlers;

import com.jaelse.seebtc.lib.assemblers.WalletBalanceHistoryAssembler;
import com.jaelse.seebtc.lib.models.WalletBalanceHistoryModelList;
import com.jaelse.seebtc.resources.WalletBalanceHistory.service.WalletBalanceHistoryService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class GetAllWalletBalanceHistoryRouteHandler implements HandlerFunction<ServerResponse> {

    private final WalletBalanceHistoryService service;
    private final WalletBalanceHistoryAssembler assembler;

    public GetAllWalletBalanceHistoryRouteHandler(WalletBalanceHistoryService service,
                                                  WalletBalanceHistoryAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return service.getAll()
                .collectList()
                .map(assembler::assemble)
                .map(list -> WalletBalanceHistoryModelList.builder()
                        .count((long) list.size())
                        .offset(0L)
                        .limit(list.size())
                        .history(list)
                        .build())
                .flatMap(models -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(models))
                );
    }
}
