package com.vob.reactive.webflux.config;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.repository.config.EnableReactiveCosmosRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableReactiveCosmosRepositories
public class CosmosdbConfig extends AbstractCosmosConfiguration {
    @Value("${cosmosdb.uri}")
    private String uri;
    @Value("${cosmosdb.key}")
    private String key;
    @Bean
    public CosmosClientBuilder appCosmosClientBuilder() {
        return new CosmosClientBuilder()
                .key(key)
                .endpoint(uri);
    }


    @Bean
    public CosmosConfig cosmosConfig() {
        return CosmosConfig.builder()
                .enableQueryMetrics(true)
                .build();
    }

    @Override
    protected String getDatabaseName() {
        return "webflux";
    }
}
