package org.seememes.bybit.tester.util;

public class ItemHistory {
    private final String name;
    private final Double price;
    private final Long time;
    private final Long count;

    public ItemHistory(String name, Double price, Long time, Long count) {
        this.name = name;
        this.price = price;
        this.time = time;
        this.count = count;
    }

    public String name() {
        return name;
    }

    public Double price() {
        return price;
    }

    public Long time() {
        return time;
    }

    public Long count() {
        return count;
    }

    @Override
    public String toString() {
        return "ItemHistory{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", time=" + time +
                ", count=" + count +
                '}';
    }
}
