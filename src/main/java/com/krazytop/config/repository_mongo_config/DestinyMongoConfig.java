package com.krazytop.config.repository_mongo_config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.krazytop.repository.destiny"},
        mongoTemplateRef = DestinyMongoConfig.MONGO_TEMPLATE
)
public class DestinyMongoConfig {
    protected static final String MONGO_TEMPLATE = "destinyMongoTemplate";
}
