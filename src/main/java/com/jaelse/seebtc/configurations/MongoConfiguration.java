package com.jaelse.seebtc.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.jaelse.seebtc.resources")
public class MongoConfiguration {
}
