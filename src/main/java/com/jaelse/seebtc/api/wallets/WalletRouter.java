package com.jaelse.seebtc.api.wallets;

import com.jaelse.seebtc.api.wallets.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class WalletRouter {

    private final CreateWalletRouteHandler createWalletRouteHandler;
    private final GetWalletRouteHandler getWalletRouteHandler;
    private final ListWalletRouteHandler listWalletRouteHandler;
    private final GetAllWalletsRouteHandler getAllWalletsRouteHandler;
    private final UpdateWalletRouteHandler updateWalletRouteHandler;
    private final DeleteWalletRouteHandler deleteWalletRouteHandler;

    @Autowired
    public WalletRouter(CreateWalletRouteHandler createWalletRouteHandler,
                        GetWalletRouteHandler getWalletRouteHandler,
                        ListWalletRouteHandler listWalletRouteHandler,
                        GetAllWalletsRouteHandler getAllWalletsRouteHandler, UpdateWalletRouteHandler updateWalletRouteHandler,
                        DeleteWalletRouteHandler deleteWalletRouteHandler) {
        this.createWalletRouteHandler = createWalletRouteHandler;
        this.getWalletRouteHandler = getWalletRouteHandler;
        this.listWalletRouteHandler = listWalletRouteHandler;
        this.getAllWalletsRouteHandler = getAllWalletsRouteHandler;
        this.updateWalletRouteHandler = updateWalletRouteHandler;
        this.deleteWalletRouteHandler = deleteWalletRouteHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> walletRoutes() {
        return route()
                .POST("/v1/wallets", createWalletRouteHandler)
                .GET("/v1/wallets/{id}", getWalletRouteHandler)
                .GET("/v1/wallets", listWalletRouteHandler)
                .GET("/v1/allWallets", getAllWalletsRouteHandler)
                .PATCH("/v1/wallets", updateWalletRouteHandler)
                .DELETE("/v1/wallets", deleteWalletRouteHandler)
                .build();
    }
}
