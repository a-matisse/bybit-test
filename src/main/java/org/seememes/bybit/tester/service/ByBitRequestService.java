package org.seememes.bybit.tester.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.market.MarketInterval;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.restApi.BybitApiAsyncMarketDataRestClient;
import com.bybit.api.client.restApi.BybitApiCallback;
import com.bybit.api.client.service.BybitApiClientFactory;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.seememes.bybit.tester.dto.ByBitMarkAnswerDto;
import org.seememes.bybit.tester.util.FuzzyTrend;
import org.seememes.bybit.tester.util.ItemHistory;
import org.seememes.bybit.tester.util.StatsAnswerDto;
import org.seememes.bybit.tester.entity.TestingStatsEntity;
import org.seememes.bybit.tester.repository.TestingStatsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

@Service
public class ByBitRequestService {
    private final static Logger log = LoggerFactory.getLogger(ByBitRequestService.class);
    private final String predictorLink;
    private final String marketInterval;
    private final Integer predictorWindowFirst;
    private final Integer predictorWindowLast;
    private final TestingStatsRepository testingStatsRepository;

    public ByBitRequestService(
            @Value("${predictor.address}") String predictorLink,
            @Value("${predictor.interval}") String marketInterval,
            @Value("${predictor.window.first}") Integer predictorWindowFirst,
            @Value("${predictor.window.last}") Integer predictorWindowLast,
            @Autowired TestingStatsRepository testingStatsRepository
    ) {
        this.predictorLink = predictorLink;
        this.marketInterval = marketInterval;
        this.testingStatsRepository = testingStatsRepository;
        this.predictorWindowFirst = predictorWindowFirst;
        this.predictorWindowLast = predictorWindowLast;
    }

    @PostConstruct
    public void testByBit() throws InterruptedException {
        int bestListSize = 30;
        double bestAccuracy = 0d;

        for (int listSize = predictorWindowFirst; listSize < predictorWindowLast; listSize++) {
            System.out.println("ТЕСТИРОВАНИЕ ТОЧНОСТИ ПРЕДСКАЗАНИЯ ДЛЯ BNB");
            System.out.println("Количество точек: " + listSize);
            System.out.println("Формат графика: " + marketInterval);

            long month = 2629800000L;
            long timeNow = System.currentTimeMillis();
            int total = 0;
            int allNum = 0;
            int successNum = 0;
            for (int j = 0; j < 6; j++) {
                long endTime = timeNow - j * month;
                long startTime = endTime - (j + 1) * month;

                List<ItemHistory> historyList = getItemHistoryForCurrencies(marketInterval, startTime, endTime);
                for (int i = listSize; i < historyList.size(); i++) {
                    total += 1;

                    List<ItemHistory> listToPredict = historyList.subList(i - listSize, i);
                    StatsAnswerDto prediction = getPredict(listToPredict);

                    double firstPrice = historyList.get(i - 1).price();
                    double secondPrice = historyList.get(i).price();
                    double priceChange = firstPrice - secondPrice;
                    FuzzyTrend predictedTrend = prediction.predictedTrend();
                    FuzzyTrend actualTrend = FuzzyTrend.fromValue(
                            firstPrice - secondPrice,
                            0,
                            0
                    );

                    allNum++;
                    boolean success = predictedTrend.equals(actualTrend);
                    if (success) {
                        successNum++;
                        log.info(
                                "priceChange: " + priceChange +
                                        " | " + predictedTrend + " == " + actualTrend +
                                        " | Prediction precision: " + prediction.chance() +
                                        " | Actual precision: " + (double) successNum / allNum +
                                        " | " + total + "/" + (historyList.size() - listSize) * 6
                        );
                    } else {
                        log.info(
                                "priceChange: " + priceChange +
                                        " | " + predictedTrend + " != " + actualTrend +
                                        " | Prediction precision: " + prediction.chance() +
                                        " | Actual precision: " + (double) successNum / allNum +
                                        " | " + total + "/" + (historyList.size() - listSize) * 6
                        );
                    }
                }
            }
            double curAccuracy = (double) successNum / allNum;
            TestingStatsEntity testingStatsEntity = new TestingStatsEntity(listSize, curAccuracy);
            testingStatsRepository.save(testingStatsEntity);
            if (curAccuracy > bestAccuracy) {
                bestListSize = listSize;
                bestAccuracy = curAccuracy;
            }
        }
        System.out.println("\nBEST ACCURACY: " + bestAccuracy);
        System.out.println("\nBEST LIST SIZE: " + bestListSize);
        System.out.println("""
                #########################################
                #########################################
                ###### THE TASK HAS BEEN COMPLETED ######
                #########################################
                #########################################
                """);
    }

    public List<ItemHistory> getItemHistoryForCurrencies(
            String marketInterval,
            long startTime,
            long endTime
    ) {
        String cryptoName1 = "BNB";
        String cryptoName2 = "USDT";

        BybitApiAsyncMarketDataRestClient client = BybitApiClientFactory.newInstance().newAsyncMarketDataRestClient();
        MarketDataRequest marketKLineRequest =
                MarketDataRequest
                        .builder()
                        .category(CategoryType.LINEAR)
                        .symbol(cryptoName1 + cryptoName2)
                        .marketInterval(MarketInterval.valueOf(marketInterval))
                        .startTime(startTime)
                        .endTime(endTime)
                        .build();

        final Object[] answer = new Object[1];
        final boolean[] ready = {false};

        client.getMarketPriceLinesData(marketKLineRequest, new BybitApiCallback<Object>() {
            @Override
            public void onResponse(Object o) {
                answer[0] = o;
                ready[0] = true;
            }

            @Override
            public void onFailure(Throwable cause) {
                BybitApiCallback.super.onFailure(cause);
                ready[0] = true;
            }
        });

        while (!ready[0]) {
            LockSupport.parkNanos(1);
        }

        if (answer[0] != null) {
            Gson gson = new Gson();

            String json = gson.toJson(answer[0]);

            ByBitMarkAnswerDto byBitMarkAnswerDto = gson.fromJson(json, ByBitMarkAnswerDto.class);

            return byBitMarkAnswerDto
                    .getResult()
                    .getList()
                    .stream()
                    .map(list -> {
                        String symbol = byBitMarkAnswerDto.getResult().getSymbol();
                        long timestamp = parseLongValue(list.get(0));
                        double highPrice = parseDoubleValue(list.get(2));
                        double lowPrice = parseDoubleValue(list.get(3));

                        double relativeChange = (highPrice + lowPrice) / 2;

                        double closePrice = parseDoubleValue(list.get(4));

                        return new ItemHistory(symbol, closePrice, timestamp, 1L);
                    })
                    .sorted(Comparator.comparingLong(ItemHistory::time))
                    .toList();
        } else return new ArrayList<>();
    }

    public StatsAnswerDto getPredict(
            List<ItemHistory> historyList
    ) throws InterruptedException {
        while (true) {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                URI currenciesURI =
                        new URIBuilder(predictorLink + "/crypto-info-with-list")
                                .addParameter("trendPrecision", Double.toString(0.001))
                                .build();
                HttpPost httpPost = new HttpPost(currenciesURI);
                httpPost.addHeader("Content-Type", "application/json");
                StringEntity stringEntity = new StringEntity(new Gson().toJson(historyList), ContentType.parse("UTF-8"));

                httpPost.setEntity(stringEntity);
                try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                    HttpEntity entity = response.getEntity();
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        String responseBody = EntityUtils.toString(entity);
                        Gson gson = new Gson();
                        return gson.fromJson(responseBody, StatsAnswerDto.class);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
            Thread.sleep(1000);
        }
    }

    private double parseDoubleValue(Object value) {
        try {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            } else if (value instanceof String) {
                return Double.parseDouble((String) value);
            } else {
                throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
            }
        } catch (ClassCastException | IllegalArgumentException e) {
            log.error(e.getMessage() + ": " + e.getCause());
            return 0.0;
        }
    }

    private long parseLongValue(Object value) {
        try {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            } else if (value instanceof String) {
                return Long.parseLong((String) value);
            } else {
                throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
            }
        } catch (ClassCastException | IllegalArgumentException e) {
            log.error(e.getMessage() + ": " + e.getCause());
            return 0L;
        }
    }
}
