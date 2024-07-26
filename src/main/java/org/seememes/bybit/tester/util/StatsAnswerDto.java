package org.seememes.bybit.tester.util;

import org.seememes.bybit.tester.util.FuzzyTrend;

public record StatsAnswerDto(FuzzyTrend predictedTrend, double chance, double price) {
}
