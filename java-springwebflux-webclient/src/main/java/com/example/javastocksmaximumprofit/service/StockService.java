package com.example.javastocksmaximumprofit.service;

import com.example.javastocksmaximumprofit.MaxProfitCalculator;
import com.example.javastocksmaximumprofit.exception.InvalidStockSymbolException;
import com.example.javastocksmaximumprofit.exception.StockServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockService {

    private final WebClient webClient;
    private final MaxProfitCalculator profitCalculator = new MaxProfitCalculator(); // 인스턴스 생성


    @Value("${alphavantage.api.key}")
    private String ALPHA_VANTAGE_API_KEY;

    /**
     * test - protected 로 변경하면 가능.
     */
    public Mono<String> fetchStockData(String symbol) {
        validateStockSymbol(symbol);

        String url = UriComponentsBuilder
                .fromHttpUrl("https://www.alphavantage.co/query")
                .queryParam("function", "TIME_SERIES_DAILY")
                .queryParam("symbol", symbol)
                .queryParam("apikey", ALPHA_VANTAGE_API_KEY)
                .toUriString();

        try {
            // WebClient의 .retrieve() 메서드는 Mono/Flux에 오류가 있을 경우 onError* 연산자로 처리할 수 있습니다.
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class);
        } catch (Exception e) {
            throw new StockServiceException("Failed to fetch stock data for symbol: " + symbol);
        }
    }

    public ResponseEntity<Double> getMaxProfit(String symbol) {

        return fetchStockData(symbol).flatMap(jsonData -> {
                    try {
                        return Mono.just(convertToStockDataList(jsonData));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(this::calculateMaximumProfit)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build()).block();

        /**
         * API 호출(동기/비동기)여야 유의미함.
         */
    }

    public Mono<Void> validateStockSymbol(String symbol) {
        if (symbol == null || symbol.isEmpty()) {
            return Mono.error(new InvalidStockSymbolException("Stock symbol is null or empty."));
        }

        String url = UriComponentsBuilder
                .fromHttpUrl("https://www.alphavantage.co/query")
                .queryParam("function", "TIME_SERIES_DAILY")
                .queryParam("symbol", symbol)
                .queryParam("apikey", ALPHA_VANTAGE_API_KEY)
                .toUriString();

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    if (response.contains("Error")) {  // 예시로 'Error Message'가 포함된 경우 오류로 가정
                        return Mono.error(new InvalidStockSymbolException("Invalid stock symbol: " + symbol));
                    }
                    return Mono.empty();  // 검증에 문제가 없을 경우, 빈 Mono를 반환
                });
    }

    /**
     *  캡슐화/은닉화 -> public 하지 않게 만들어야함.
     */
    private List<StockData> convertToStockDataList(String json) throws IOException {

        /**
         *  null 아님.
         */
        List<StockData> stockDataList = new ArrayList<>();

        // Jackson ObjectMapper를 사용하여 JSON 문자열을 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        // "Time Series (Daily)" 내의 데이터 접근
        JsonNode timeSeriesNode = rootNode.path("Time Series (Daily)");
        Iterator<Map.Entry<String, JsonNode>> fields = timeSeriesNode.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            LocalDate date = LocalDate.parse(field.getKey());  // 문자열을 LocalDate로 변환
            double closePrice = field.getValue().path("4. close").asDouble();

            // 추출한 데이터로 StockData 객체 생성 및 리스트에 추가
            StockData stockData = new StockData(date, closePrice);
            stockDataList.add(stockData);
        }

        return stockDataList;
    }

    private double calculateMaximumProfit(List<StockData> stockDataList) {
        try {
            /**
             *  stockDataList.forEach(System.out::println);
             */
            return profitCalculator.stocksMaxProfitCalculator(stockDataList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
