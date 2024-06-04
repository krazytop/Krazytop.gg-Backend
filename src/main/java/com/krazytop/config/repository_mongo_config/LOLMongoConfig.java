package com.krazytop.config.repository_mongo_config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.krazytop.repository.lol"},
        mongoTemplateRef = LOLMongoConfig.MONGO_TEMPLATE
)
public class LOLMongoConfig {
    protected static final String MONGO_TEMPLATE = "lolMongoTemplate";
}
