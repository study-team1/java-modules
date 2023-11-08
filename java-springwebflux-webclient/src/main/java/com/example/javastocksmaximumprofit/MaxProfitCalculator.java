package com.example.javastocksmaximumprofit;

import com.example.javastocksmaximumprofit.service.StockData;

import java.util.List;

/**
 * 빈으로 등록.
 */
public class MaxProfitCalculator {

    public double stocksMaxProfitCalculator(List<StockData> stockDataList) throws Exception {
        /**
         * stockDataList == null -> new ArrayList<>();
         */
        // 메서드 분리 예정
        if (stockDataList.size() < 2) {
            throw new Exception();
        }

        int minPriceIndex = 0;
        double minPrice = stockDataList.get(minPriceIndex).getPrice();
        double maxProfit = 0.0;

        for (int i = 1; i < stockDataList.size(); i++) {
            double currentPrice = stockDataList.get(i).getPrice();

            if (currentPrice < minPrice) {
                minPrice = currentPrice;
                minPriceIndex = i;
            } else if (i > minPriceIndex) {
                double currentProfit = currentPrice - minPrice;
                maxProfit = Math.max(maxProfit, currentProfit);
            }
        }

        return maxProfit;
    }

    static int maxProfit(int prices[], int size) {
        int maxProfit = 0;

        for (int i = 1; i < size; i++) {
            if (prices[i] > prices[i - 1]) {
                maxProfit += prices[i] - prices[i - 1];
            }
        }

        return maxProfit;
    }

}
