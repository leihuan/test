package com.example.postnumbersearch;

import java.util.List;

/**
 * Created by lh on 2017/8/2.
 */
public class DataBean {
    int total;
    List<AddressInfo> result;
    String SingerResult;
    int error_code;
    String reason;

    public int getTotal() {
        return total;
    }

    public List<AddressInfo> getResult() {
        return result;
    }

    public String getSingerResult() {
        return SingerResult;
    }

    public int getError_code() {
        return error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setResult(List<AddressInfo> result) {
        this.result = result;
    }

    public void setSingerResult(String singerResult) {
        SingerResult = singerResult;
    }
}
