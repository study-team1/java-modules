package com.example.javastocksmaximumprofit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {

        return WebClient.builder().build();

        /**
         * return WebClient.builder()
         *                 .baseUrl("https://api.example.com")
         *                 .build();
         */

        /**
         * return WebClient.builder()
         *                 .baseUrl("https://api.example.com")
         *                 .filter(ExchangeFilterFunction.ofRequestProcessor(
         *                     clientRequest -> {
         *                         log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
         *                         return Mono.just(clientRequest);
         *                     }))
         *                 .build();
         */

        /**
         * ExchangeStrategies strategies = ExchangeStrategies.builder()
         *                 .codecs(configurer -> {
         *                     // Custom configuration here
         *                 }).build();
         *
         * return WebClient.builder()
         *                 .baseUrl("https://api.example.com")
         *                 .exchangeStrategies(strategies)
         *                 .build();
         */

        /**
         * return WebClient.builder()
         *                 .baseUrl("https://api.example.com")
         *                 .clientConnector(new ReactorClientHttpConnector(
         *                     HttpClient.create().responseTimeout(Duration.ofMillis(10000))))
         *                 .build();
         */
    }

}
