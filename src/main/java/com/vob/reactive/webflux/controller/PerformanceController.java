package com.vob.reactive.webflux.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("performance")
public class PerformanceController {
    final String myIp = "https://fslow.azurewebsites.net/api/slow";
    final RestTemplate restTemplate = new RestTemplate();
    final WebClient client = WebClient.builder().baseUrl(myIp).build();

    @GetMapping("sync")
    public HttpEntity<String> sync() {
        return restTemplate.getForEntity(myIp, String.class);
    }

    @GetMapping("async")
    public Mono<String> async() {
        return client.get()
                .exchange()
                .flatMap(b-> b.bodyToMono(String.class));
    }
}
