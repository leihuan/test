package com.example.postnumbersearch;

/**
 * Created by lh on 2017/8/2.
 */
public interface OnHttpRequestListener {
    void onStart();
    void onSuccess(DataBean data);
    void onError(String errorMsg);
}
