package com.example.javastocksmaximumprofit.exception;

public class InvalidStockSymbolException extends RuntimeException {

    public InvalidStockSymbolException(String message) {
        super(message);
    }

}
