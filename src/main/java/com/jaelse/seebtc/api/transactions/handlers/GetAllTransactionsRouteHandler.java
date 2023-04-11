package com.jaelse.seebtc.api.transactions.handlers;

import com.jaelse.seebtc.lib.assemblers.TransactionAssembler;
import com.jaelse.seebtc.lib.assemblers.WalletAssembler;
import com.jaelse.seebtc.lib.models.TransactionModelList;
import com.jaelse.seebtc.lib.models.WalletModelList;
import com.jaelse.seebtc.resources.transactions.service.TransactionService;
import com.jaelse.seebtc.resources.wallets.service.WalletService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class GetAllTransactionsRouteHandler implements HandlerFunction<ServerResponse> {

    private final TransactionService service;
    private final TransactionAssembler assembler;

    public GetAllTransactionsRouteHandler(TransactionService service, TransactionAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return service.getAll().collectList()
                .map(assembler::assemble)
                .map(list -> TransactionModelList.builder()
                        .limit(list.size())
                        .offset(0L)
                        .transactions(list)
                        .count((long) list.size())
                        .build())
                .flatMap(entity -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(entity))
                );
    }

}
