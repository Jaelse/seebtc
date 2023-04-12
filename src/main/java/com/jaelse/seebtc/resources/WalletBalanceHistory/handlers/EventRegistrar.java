package com.jaelse.seebtc.resources.WalletBalanceHistory.handlers;

import com.jaelse.seebtc.lib.events.EventHandler;
import reactor.util.function.Tuple2;

import java.util.HashMap;
import java.util.Map;

public class EventRegistrar {

    private final Map<String, EventHandler> register;

    public EventRegistrar() {
        this.register = new HashMap<>();
    }

    public EventRegistrar addHandler(String topic, EventHandler handler) {
        register.put(topic, handler);
        return this;
    }

    public EventHandler get(String topic) {
        return register.get(topic);
    }


}
