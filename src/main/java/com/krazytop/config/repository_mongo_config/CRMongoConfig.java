package com.krazytop.config.repository_mongo_config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.krazytop.repository.clash_royal"},
        mongoTemplateRef = CRMongoConfig.MONGO_TEMPLATE
)
public class CRMongoConfig {
    protected static final String MONGO_TEMPLATE = "clashRoyalMongoTemplate";
}
