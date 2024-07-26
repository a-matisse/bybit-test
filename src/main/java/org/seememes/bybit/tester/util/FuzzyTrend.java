package org.seememes.bybit.tester.util;

public enum FuzzyTrend {
    DOWN("down"),
    SLIGHTLY_DOWN("slightly down"),
    EQUAL("equal"),
    SLIGHTLY_UP("slightly up"),
    UP("up");

    private final String description;

    FuzzyTrend(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // Метод для получения тренда на основе значения и len
    public static FuzzyTrend fromValue(double priceChange, double len, double precision) {
        if (priceChange <= -precision * len) {
            return DOWN;
        } else if (priceChange > -precision * len && priceChange <= precision * len) {
            return EQUAL;
        } else {
            return UP;
        }
    }

    public static FuzzyTrend fromValuePrecised(double priceChange, double len) {
        if (priceChange <= -1.5 * len) {
            return DOWN;
        } else if (priceChange > -1.5 * len && priceChange <= -0.5 * len) {
            return SLIGHTLY_DOWN;
        } else if (priceChange > -0.5 * len && priceChange <= 0.5 * len) {
            return EQUAL;
        } else if (priceChange > 0.5 * len && priceChange <= 1.5 * len) {
            return SLIGHTLY_UP;
        } else {
            return UP;
        }
    }
}
