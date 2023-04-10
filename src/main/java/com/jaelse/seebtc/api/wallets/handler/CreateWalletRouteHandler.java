package com.jaelse.seebtc.api.wallets.handler;

import com.jaelse.seebtc.lib.assemblers.WalletAssembler;
import com.jaelse.seebtc.lib.dtos.CreateWalletDto;
import com.jaelse.seebtc.resources.wallets.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CreateWalletRouteHandler implements HandlerFunction<ServerResponse> {

    private final WalletService service;
    private final WalletAssembler assembler;

    @Autowired
    public CreateWalletRouteHandler(WalletService service, WalletAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }


    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.bodyToMono(CreateWalletDto.class)
                .flatMap(service::createWallet)
                .map(assembler::assemble)
                .flatMap(entity -> ServerResponse
                        .created(URI.create("http://localhost:8080/v1/wallets/" + entity.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(entity))
                )
                .switchIfEmpty(Mono.error(new ServerWebInputException("Request body cannot be empty.")));
    }
}
