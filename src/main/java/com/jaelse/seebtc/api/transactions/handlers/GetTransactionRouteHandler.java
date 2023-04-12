package com.jaelse.seebtc.api.transactions.handlers;

import com.jaelse.seebtc.lib.assemblers.TransactionAssembler;
import com.jaelse.seebtc.resources.transactions.service.TransactionService;
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

import java.net.URI;

@Component
public class GetTransactionRouteHandler implements HandlerFunction<ServerResponse> {

    private final TransactionService service;
    private final TransactionAssembler assembler;

    @Autowired
    public GetTransactionRouteHandler(TransactionService service, TransactionAssembler assembler) {
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
                        .body(BodyInserters.fromValue(model))
                )
                .onErrorResume(throwable -> ServerResponse
                        .status(HttpStatus.BAD_REQUEST)
                        .body(BodyInserters.fromValue("Error while handling the request: " + throwable.getCause()))
                );
    }

}
