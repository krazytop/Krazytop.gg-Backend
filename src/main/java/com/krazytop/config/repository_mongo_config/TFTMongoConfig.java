package com.krazytop.config.repository_mongo_config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.krazytop.repository.tft"},
        mongoTemplateRef = TFTMongoConfig.MONGO_TEMPLATE
)
public class TFTMongoConfig {
    protected static final String MONGO_TEMPLATE = "tftMongoTemplate";
}
