package com.example.javastocksmaximumprofit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidStockSymbolException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidStockSymbolException(InvalidStockSymbolException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(StockServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleStockServiceException(StockServiceException ex) {
        return ex.getMessage();
    }
}
