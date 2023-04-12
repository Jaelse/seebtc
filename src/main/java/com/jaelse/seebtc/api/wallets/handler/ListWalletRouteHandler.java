package com.jaelse.seebtc.api.wallets.handler;

import com.jaelse.seebtc.lib.assemblers.WalletAssembler;
import com.jaelse.seebtc.lib.models.WalletModelList;
import com.jaelse.seebtc.lib.queries.WalletQuery;
import com.jaelse.seebtc.resources.wallets.service.WalletService;
import org.bson.types.ObjectId;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
public class ListWalletRouteHandler implements HandlerFunction<ServerResponse> {

    private final WalletService service;
    private final WalletAssembler assembler;

    public ListWalletRouteHandler(WalletService service, WalletAssembler assembler) {
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
                        .map(listNCount -> WalletModelList.builder()
                                .limit(query.getLimit())
                                .offset(query.getOffset())
                                .wallets(listNCount.getT1())
                                .count(listNCount.getT2())
                                .build()))
                .flatMap(entity -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(entity))
                );
    }

}
