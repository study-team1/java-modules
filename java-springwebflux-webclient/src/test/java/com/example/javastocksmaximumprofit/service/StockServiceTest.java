package com.example.javastocksmaximumprofit.service;

import com.example.javastocksmaximumprofit.exception.InvalidStockSymbolException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class StockServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private StockService stockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("mocked response"));
     }

    @Test
    void test01() {
        //given
        String symbol = "AAPL";

        //when
        Mono<String> result = stockService.fetchStockData(symbol);

        //then
        assertEquals("mocked response", result.block());
    }

    @Test
    void validateStockSymbol_WithInvalidLength_ShouldThrowException() {
        String invalidSymbol = "INVALID";

        StepVerifier.create(stockService.fetchStockData(invalidSymbol))
                .expectError(InvalidStockSymbolException.class)
                .verify();
    }

    @Test
    void validateStockSymbol_WithNullSymbol_ShouldThrowException() {
        String nullSymbol = null;

        StepVerifier.create(stockService.fetchStockData(nullSymbol))
                .expectError(InvalidStockSymbolException.class)
                .verify();
    }

    @Test
    void validateStockSymbol_WithInvalidLength_ShouldThrowException2() {
        String invalidSymbol = "INVALID";

        assertThrows(InvalidStockSymbolException.class, () -> {
            stockService.fetchStockData(invalidSymbol);
        });
    }

    @Test
    void validateStockSymbol_WithNullSymbol_ShouldThrowException2() {
        String nullSymbol = null;

        assertThrows(InvalidStockSymbolException.class, () -> {
            stockService.fetchStockData(nullSymbol);
        });
    }
}