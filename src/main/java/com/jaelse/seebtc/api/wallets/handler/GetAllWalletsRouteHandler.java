package com.jaelse.seebtc.api.wallets.handler;

import com.jaelse.seebtc.lib.assemblers.WalletAssembler;
import com.jaelse.seebtc.lib.models.WalletModelList;
import com.jaelse.seebtc.resources.wallets.service.WalletService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class GetAllWalletsRouteHandler implements HandlerFunction<ServerResponse> {

    private final WalletService service;
    private final WalletAssembler assembler;

    public GetAllWalletsRouteHandler(WalletService service, WalletAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return service.getAllWallets()
                .collectList()
                .map(assembler::assemble)
                .map(list -> WalletModelList.builder()
                        .limit(list.size())
                        .offset(0L)
                        .wallets(list)
                        .count((long) list.size())
                        .build())
                .flatMap(entity -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(entity))
                );
    }

}
