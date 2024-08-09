package org.seememes.bybit.tester.entity;

import jakarta.persistence.*;

@Entity
public class TestingStatsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private Integer listSize;
    @Column
    private Double accuracy;
    @Column
    private String timeLimit;

    public TestingStatsEntity() {
    }

    public TestingStatsEntity(Integer listSize, Double accuracy, String timeLimit) {
        this.listSize = listSize;
        this.accuracy = accuracy;
        this.timeLimit = timeLimit;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Integer getListSize() {
        return listSize;
    }

    public void setListSize(Integer window) {
        this.listSize = window;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }
}
