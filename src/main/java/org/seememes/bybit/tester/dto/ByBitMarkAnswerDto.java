package org.seememes.bybit.tester.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ByBitMarkAnswerDto {
    @SerializedName("retCode")
    private int retCode;

    @SerializedName("retMsg")
    private String retMsg;

    @SerializedName("result")
    private Result result;

    @SerializedName("retExtInfo")
    private Map<String, Object> retExtInfo;

    @SerializedName("time")
    private long time;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Map<String, Object> getRetExtInfo() {
        return retExtInfo;
    }

    public void setRetExtInfo(Map<String, Object> retExtInfo) {
        this.retExtInfo = retExtInfo;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ByBitMarkAnswerDto{" +
                "retCode=" + retCode +
                ", retMsg='" + retMsg + '\'' +
                ", result=" + result +
                ", retExtInfo=" + retExtInfo +
                ", time=" + time +
                '}';
    }

    public static class Result {
        @SerializedName("symbol")
        private String symbol;

        @SerializedName("category")
        private String category;

        @SerializedName("list")
        private List<List<Object>> list;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public List<List<Object>> getList() {
            return list;
        }

        public void setList(List<List<Object>> list) {
            this.list = list;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "symbol='" + symbol + '\'' +
                    ", category='" + category + '\'' +
                    ", list=" + list +
                    '}';
        }
    }
}