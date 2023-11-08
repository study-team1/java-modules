package com.example.javastocksmaximumprofit.controller;

import com.example.javastocksmaximumprofit.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

class StockControllerTest {

    @Mock
    private StockService stockService;

    @InjectMocks
    private StockController stockController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // To initialize mocks if not yet done
        this.webTestClient = WebTestClient.bindToController(stockController).build();
    }

    @Test
    void getStockDataTest() throws Exception {
        String symbol = "AAPL";
        String expectedData = "Some data";

        when(stockService.fetchStockData(symbol)).thenReturn(Mono.just(expectedData));

        webTestClient.get().uri("/stocks/" + symbol)
                .exchange()  // This initiates the request
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(expectedData);
    }

}