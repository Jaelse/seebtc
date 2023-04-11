package com.jaelse.seebtc.api.transactions;

import com.jaelse.seebtc.api.transactions.handlers.CreateTransactionRouteHandler;
import com.jaelse.seebtc.api.transactions.handlers.GetAllTransactionsRouteHandler;
import com.jaelse.seebtc.api.transactions.handlers.GetTransactionRouteHandler;
import com.jaelse.seebtc.api.transactions.handlers.ListTransactionsRouteHandler;
import com.jaelse.seebtc.api.wallets.handler.*;
import org.apache.kafka.clients.admin.internals.ListTransactionsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TransactionRouter {

    private final CreateTransactionRouteHandler createTransactionRouteHandler;
    private final GetAllTransactionsRouteHandler getAllTransactionsRouteHandler;
    private final GetTransactionRouteHandler getTransactionRouteHandler;
    private final ListTransactionsRouteHandler listTransactionsHandler;

    @Autowired
    public TransactionRouter(CreateTransactionRouteHandler createTransactionRouteHandler,
                             GetAllTransactionsRouteHandler getAllTransactionsRouteHandler,
                             GetTransactionRouteHandler getTransactionRouteHandler,
                             ListTransactionsRouteHandler listTransactionsHandler) {
        this.createTransactionRouteHandler = createTransactionRouteHandler;
        this.getAllTransactionsRouteHandler = getAllTransactionsRouteHandler;
        this.getTransactionRouteHandler = getTransactionRouteHandler;
        this.listTransactionsHandler = listTransactionsHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> transactionRoutes() {
        return route()
                .POST("/v1/wallets/{id}/transfer", createTransactionRouteHandler)
                .GET("/v1/transactions/{id}", getTransactionRouteHandler)
                .GET("/v1/transactions", listTransactionsHandler)
                .GET("/v1/allTransactions", getAllTransactionsRouteHandler)
                .build();
    }
}
