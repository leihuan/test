package com.example.postnumbersearch;

import android.app.Application;

import com.baidu.appx.BaiduAppX;

/**
 * Created by lh on 2017/8/4.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BaiduAppX.version();

    }
}
