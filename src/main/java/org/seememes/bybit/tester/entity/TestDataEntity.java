package org.seememes.bybit.tester.entity;

import jakarta.persistence.*;

@Entity
public class TestDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    private String data;

    public TestDataEntity() {
    }

    public TestDataEntity(String data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
