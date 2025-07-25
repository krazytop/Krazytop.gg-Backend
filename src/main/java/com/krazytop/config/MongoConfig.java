package com.krazytop.config;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;


@Configuration
public class MongoConfig {

    @Primary
    @Bean(name = "tftMongoProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.tft")
    public MongoProperties getTftMongoProperties() {
        return new MongoProperties();
    }

    @Bean(name = "lolMongoProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.lol")
    public MongoProperties getLolMongoProperties() {
        return new MongoProperties();
    }

    @Bean(name = "clashRoyalMongoProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.clash-royal")
    public MongoProperties getClashRoyalMongoProperties() {
        return new MongoProperties();
    }

    @Bean(name = "apiKeyMongoProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.api-key")
    public MongoProperties getApiKeyMongoProperties() {
        return new MongoProperties();
    }

    @Primary
    @Bean(name = "tftMongoTemplate")
    public MongoTemplate tftMongoTemplate() {
        return new MongoTemplate(tftMongoDatabaseFactory(getTftMongoProperties()));
    }

    @Bean(name = "lolMongoTemplate")
    public MongoTemplate lolMongoTemplate() {
        return new MongoTemplate(lolMongoDatabaseFactory(getLolMongoProperties()));
    }

    @Bean(name = "clashRoyalMongoTemplate")
    public MongoTemplate clashRoyalMongoTemplate() {
        return new MongoTemplate(clashRoyalMongoDatabaseFactory(getClashRoyalMongoProperties()));
    }

    @Bean(name = "apiKeyMongoTemplate")
    public MongoTemplate apiKeyMongoTemplate() {
        return new MongoTemplate(apiKeyMongoDatabaseFactory(getApiKeyMongoProperties()));
    }

    @Primary
    @Bean
    public MongoDatabaseFactory tftMongoDatabaseFactory(MongoProperties mongo) {
        return new SimpleMongoClientDatabaseFactory(
                mongo.getUri()
        );
    }

    @Bean
    public MongoDatabaseFactory lolMongoDatabaseFactory(MongoProperties mongo) {
        return new SimpleMongoClientDatabaseFactory(
                mongo.getUri()
        );
    }

    @Bean
    public MongoDatabaseFactory clashRoyalMongoDatabaseFactory(MongoProperties mongo) {
        return new SimpleMongoClientDatabaseFactory(
                mongo.getUri()
        );
    }

    @Bean
    public MongoDatabaseFactory apiKeyMongoDatabaseFactory(MongoProperties mongo) {
        return new SimpleMongoClientDatabaseFactory(
                mongo.getUri()
        );
    }

}
