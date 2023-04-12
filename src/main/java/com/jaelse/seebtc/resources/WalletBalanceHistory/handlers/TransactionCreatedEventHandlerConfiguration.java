package com.jaelse.seebtc.resources.WalletBalanceHistory.handlers;

import com.jaelse.seebtc.lib.events.*;
import com.jaelse.seebtc.resources.WalletBalanceHistory.service.WalletBalanceHistoryService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.*;

@Configuration
public class TransactionCreatedEventHandlerConfiguration {

    private final WalletBalanceHistoryService walletBalanceHistoryService;
    private final Logger logger = LoggerFactory.getLogger(TransactionCreatedEventHandlerConfiguration.class);


    @Autowired
    public TransactionCreatedEventHandlerConfiguration(WalletBalanceHistoryService walletBalanceHistoryService) {
        this.walletBalanceHistoryService = walletBalanceHistoryService;
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, BaseEvent<?>> reactiveKafkaConsumerTemplate() {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Replace with your Kafka bootstrap servers
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.jaelse.seebtc.lib.events.BaseEvent");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "wallet_balance_history");
        consumerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, "wallet_balance_history-1");
        var options = ReceiverOptions.<String, BaseEvent<?>>create(consumerProps)
                .subscription(List.of(Topic.TRANSACTION_CREATED, Topic.WALLET_CREATED));
        return new ReactiveKafkaConsumerTemplate<String, BaseEvent<?>>(options);
    }

    @Bean
    public EventRegistrar registrar() {
        return new EventRegistrar()
                .addHandler(Topic.TRANSACTION_CREATED, new TransactionCreatedEventHandler(walletBalanceHistoryService))
                .addHandler(Topic.WALLET_CREATED, new WalletCreatedEventHandler(walletBalanceHistoryService));
    }

    @EventListener
    public Disposable startConsuming(ContextRefreshedEvent event) {
        return reactiveKafkaConsumerTemplate()
                .receiveAutoAck()
                .doOnNext(consumerRecord -> logger.info("received key={}, value={} from topic={}, offset={}",
                        consumerRecord.key(),
                        consumerRecord.value(),
                        consumerRecord.topic(),
                        consumerRecord.offset())
                )
                .flatMap(stringBaseEventConsumerRecord -> handler(
                        stringBaseEventConsumerRecord.topic(),
                        stringBaseEventConsumerRecord.key(),
                        stringBaseEventConsumerRecord.value()))
                .subscribe();

    }


    Mono<Void> handler(String topic, String key, BaseEvent<?> evt) {
        var registrar = registrar();
        return switch (topic) {
            case Topic.WALLET_CREATED -> registrar.get(topic)
                    .handle(new ObjectId(key), (WalletCreatedEvent) evt);
            case Topic.TRANSACTION_CREATED -> registrar.get(topic)
                    .handle(new ObjectId(key), (TransactionCreatedEvent) evt);
            default -> Mono.error(new ClassNotFoundException());
        };
    }
}
