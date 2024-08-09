package org.seememes.bybit.tester.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class SecondTestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private Long time;
    @Column
    private Double priceChange;
    @Column
    private Integer predictedTrend;
    @Column
    private Integer actualTrend;
    @Column
    private Double predictedAccuracy;
    @Column
    private Double actualAccuracy;

    public SecondTestEntity(Long time, Integer predictedTrend, Integer actualTrend, Double priceChange, Double predictedAccuracy, Double actualAccuracy) {
        this.time = time;
        this.predictedTrend = predictedTrend;
        this.actualTrend = actualTrend;
        this.predictedAccuracy = predictedAccuracy;
    }

    public SecondTestEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getPredictedTrend() {
        return predictedTrend;
    }

    public void setPredictedTrend(Integer predictedTrend) {
        this.predictedTrend = predictedTrend;
    }

    public Integer getActualTrend() {
        return actualTrend;
    }

    public void setActualTrend(Integer actualTrend) {
        this.actualTrend = actualTrend;
    }

    public Double getPredictedAccuracy() {
        return predictedAccuracy;
    }

    public void setPredictedAccuracy(Double predictedAccuracy) {
        this.predictedAccuracy = predictedAccuracy;
    }

    public Double getActualAccuracy() {
        return actualAccuracy;
    }

    public void setActualAccuracy(Double actualAccuracy) {
        this.actualAccuracy = actualAccuracy;
    }

    public Double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(Double priceChange) {
        this.priceChange = priceChange;
    }

    @Override
    public String toString() {
        return "time: " + time +
                " | " + predictedTrend + " != " + actualTrend +
                " | Price change: " + priceChange +
                " | Predicted accuracy: " + predictedAccuracy +
                " | Actual accuracy: " + actualAccuracy;
    }
}
