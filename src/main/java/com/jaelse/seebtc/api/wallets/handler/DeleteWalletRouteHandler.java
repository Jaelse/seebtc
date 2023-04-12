package com.jaelse.seebtc.api.wallets.handler;

import com.jaelse.seebtc.lib.assemblers.WalletAssembler;
import com.jaelse.seebtc.resources.wallets.service.WalletService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class DeleteWalletRouteHandler implements HandlerFunction<ServerResponse> {

    private final WalletService service;
    private final WalletAssembler assembler;

    @Autowired
    public DeleteWalletRouteHandler(WalletService service, WalletAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }


    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return Mono.just(new ObjectId(request.pathVariable("id")))
                .flatMap(service::delete)
                .map(assembler::assemble)
                .flatMap(entity -> ServerResponse
                        .ok()
                        .body(BodyInserters.fromValue(entity)))
                .onErrorResume(throwable -> ServerResponse
                        .status(HttpStatus.BAD_REQUEST)
                        .body(BodyInserters.fromValue("Error while handling the request: " + throwable.getCause()))
                );
    }
}
