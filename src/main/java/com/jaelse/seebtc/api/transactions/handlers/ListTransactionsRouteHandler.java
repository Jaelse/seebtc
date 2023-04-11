package com.jaelse.seebtc.api.transactions.handlers;

import com.jaelse.seebtc.lib.assemblers.TransactionAssembler;
import com.jaelse.seebtc.lib.assemblers.WalletAssembler;
import com.jaelse.seebtc.lib.models.TransactionModelList;
import com.jaelse.seebtc.lib.models.WalletModelList;
import com.jaelse.seebtc.lib.queries.WalletQuery;
import com.jaelse.seebtc.resources.transactions.service.TransactionService;
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
public class ListTransactionsRouteHandler implements HandlerFunction<ServerResponse> {

    private final TransactionService service;
    private final TransactionAssembler assembler;

    public ListTransactionsRouteHandler(TransactionService service, TransactionAssembler assembler) {
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
                        .map(listNCount -> TransactionModelList.builder()
                                .limit(query.getLimit())
                                .offset(query.getOffset())
                                .transactions(listNCount.getT1())
                                .count(listNCount.getT2())
                                .build()))
                .flatMap(entity -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(entity))
                );
    }


}
