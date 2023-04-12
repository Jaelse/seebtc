package com.jaelse.seebtc.lib.events;

import org.bson.types.ObjectId;

import java.util.Optional;

public interface BaseEvent<T> {

    Optional<ObjectId> key();

    T payload();
}
