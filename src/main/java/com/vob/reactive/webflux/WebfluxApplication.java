package com.vob.reactive.webflux;

import com.azure.spring.data.cosmos.repository.config.EnableReactiveCosmosRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableReactiveCosmosRepositories
public class WebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxApplication.class, args);
    }

}
