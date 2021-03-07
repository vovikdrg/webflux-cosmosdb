package com.vob.reactive.webflux;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

//@WebFluxTest(controllers = ProfileController.class)
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class Tests {
    @Autowired
    private WebTestClient webClient;
    Faker faker = new Faker();

    @Test
    public void testIfCanCreateList() {
        var profileName = faker.name().username();
        var listName = faker.book().title();
        webClient.get().uri("/profile/" + profileName)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.size()").isEqualTo(0);
        webClient.post()
                .uri("/profile/" + profileName + "?name=" + listName)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(listName)
        ;
    }
}