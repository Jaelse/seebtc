package com.jaelse.seebtc.lib.events;

import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

public interface EventHandler<T> {

    Mono<Void> handle(ObjectId key, T evt);
}
