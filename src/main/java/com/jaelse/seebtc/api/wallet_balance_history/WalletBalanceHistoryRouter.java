package com.jaelse.seebtc.api.wallet_balance_history;

import com.jaelse.seebtc.api.wallet_balance_history.handlers.GetAllWalletBalanceHistoryRouteHandler;
import com.jaelse.seebtc.api.wallet_balance_history.handlers.GetBalanceHistoryRouteHandler;
import com.jaelse.seebtc.api.wallet_balance_history.handlers.ListBalanceHistoryRouteHandler;
import com.jaelse.seebtc.api.wallet_balance_history.handlers.WalletHistoryBetweenHoursRouteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class WalletBalanceHistoryRouter {

    private final GetAllWalletBalanceHistoryRouteHandler getAllWalletBalanceHistoryRouteHandler;
    private final GetBalanceHistoryRouteHandler getBalanceHistoryRouteHandler;
    private final ListBalanceHistoryRouteHandler listBalanceHistoryRouteHandler;
    private final WalletHistoryBetweenHoursRouteHandler walletHistoryBetweenHoursRouteHandler;

    @Autowired
    public WalletBalanceHistoryRouter(GetAllWalletBalanceHistoryRouteHandler getAllWalletBalanceHistoryRouteHandler,
                                      GetBalanceHistoryRouteHandler getBalanceHistoryRouteHandler,
                                      ListBalanceHistoryRouteHandler listBalanceHistoryRouteHandler,
                                      WalletHistoryBetweenHoursRouteHandler walletHistoryBetweenHoursRouteHandler) {
        this.getAllWalletBalanceHistoryRouteHandler = getAllWalletBalanceHistoryRouteHandler;
        this.getBalanceHistoryRouteHandler = getBalanceHistoryRouteHandler;
        this.listBalanceHistoryRouteHandler = listBalanceHistoryRouteHandler;
        this.walletHistoryBetweenHoursRouteHandler = walletHistoryBetweenHoursRouteHandler;
    }


    @Bean
    public RouterFunction<ServerResponse> walletBalanceHistoryRoutes() {
        return route()
                .GET("/v1/allBalance", getAllWalletBalanceHistoryRouteHandler)
                .GET("/v1/balance/{id}", getBalanceHistoryRouteHandler)
                .GET("/v1/balances", listBalanceHistoryRouteHandler)
                .GET("/v1/wallet/{walletId}/balance", walletHistoryBetweenHoursRouteHandler)
                .build();
    }
}
