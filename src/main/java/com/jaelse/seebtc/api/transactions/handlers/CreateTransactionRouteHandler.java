package com.jaelse.seebtc.api.transactions.handlers;

import com.jaelse.seebtc.api.wallets.handler.CreateWalletRouteHandler;
import com.jaelse.seebtc.lib.assemblers.TransactionAssembler;
import com.jaelse.seebtc.lib.dtos.CreateTransactionDto;
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
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class CreateTransactionRouteHandler implements HandlerFunction<ServerResponse> {

    private final TransactionService service;
    private final TransactionAssembler assembler;

    @Autowired
    public CreateTransactionRouteHandler(TransactionService service, TransactionAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }


    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.bodyToMono(CreateTransactionDto.class)
                .zipWith(Mono.just(new ObjectId(request.pathVariable("id"))))
                .flatMap(dtoNId -> service.create(dtoNId.getT2(), dtoNId.getT1()))
                .map(assembler::assemble)
                .flatMap(entity -> ServerResponse
                        .created(URI.create("http://localhost:8080/v1/wallets/" + entity.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(entity))
                )
                .switchIfEmpty(Mono.error(new ServerWebInputException("Invalid request body.")))
                .onErrorResume(throwable -> ServerResponse
                        .status(HttpStatus.BAD_REQUEST)
                        .body(BodyInserters.fromValue("Error while handling the request: " + throwable.getCause()))
                );
    }
}
