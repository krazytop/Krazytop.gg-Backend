package com.krazytop.config.repository_mongo_config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.krazytop.repository.api_key"},
        mongoTemplateRef = ApiKeyMongoConfig.MONGO_TEMPLATE
)
public class ApiKeyMongoConfig {
    protected static final String MONGO_TEMPLATE = "apiKeyMongoTemplate";
}
