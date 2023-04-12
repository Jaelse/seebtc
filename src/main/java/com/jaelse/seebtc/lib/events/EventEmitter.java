package com.jaelse.seebtc.lib.events;

import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

public class EventEmitter {

    private final ReactiveKafkaProducerTemplate<String, BaseEvent<?>> reactiveKafkaProducerTemplate;

    public EventEmitter(ReactiveKafkaProducerTemplate<String, BaseEvent<?>> reactiveKafkaProducerTemplate) {
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
    }


    public Mono<SenderResult<Void>> emit(String topic, BaseEvent<?> event) {
        return event.key().map(key -> reactiveKafkaProducerTemplate.send(topic, key.toHexString(), event))
                .orElse(reactiveKafkaProducerTemplate.send(topic, event));
    }
}
