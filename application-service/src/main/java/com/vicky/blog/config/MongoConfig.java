package com.vicky.blog.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import java.util.Collection;
import java.util.Collections;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(
        "mongodb://localhost:27017/test"
        );
        MongoClientSettings mongoClientSettings = MongoClientSettings
        .builder()
        .applyConnectionString(connectionString)
        .build();

        return MongoClients.create(mongoClientSettings);
    }
    
    @Override
    protected Collection<String> getMappingBasePackages() {
        return Collections.singleton("com.vicky.blog");
    }
}
