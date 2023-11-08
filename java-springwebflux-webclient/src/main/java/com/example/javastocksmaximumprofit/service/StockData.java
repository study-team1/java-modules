package com.example.javastocksmaximumprofit.service;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StockData {
    private final LocalDate date;
    private final double price;

    public StockData(LocalDate date, double closePrice) {
        this.date = date;
        this.price = closePrice;
    }
}