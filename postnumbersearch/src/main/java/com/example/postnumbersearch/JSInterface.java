package com.example.postnumbersearch;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by lh on 2017/8/9.
 */
public class JSInterface {

    private WebView webView;

    private Handler mHandler;

    public final static int setLocationStr = 0;
    public final static int setLocationArray = 1;
    public final static int showProgressBar = 3;
    public final static int dissmissProgressBar = 4;

    public JSInterface(WebView webview, Handler handler){
        this.webView = webview;
        this.mHandler = handler;
    }

    @android.webkit.JavascriptInterface
    public void setLocations(final String str){
        if(mHandler == null){
            return;
        }
        Message msg = mHandler.obtainMessage();
        msg.what = setLocationStr;
        msg.obj = str;
        mHandler.sendMessage(msg);
    }

    @android.webkit.JavascriptInterface
    public void setLocation(final String[] strArray){
        if(mHandler == null){
            return;
        }
        Message msg = mHandler.obtainMessage();
        msg.what = setLocationArray;
        msg.obj = strArray;
        mHandler.sendMessage(msg);

    }
    @android.webkit.JavascriptInterface
    public void  showProgressBar(){
        mHandler.sendEmptyMessage(showProgressBar);
    }

    @android.webkit.JavascriptInterface
    public void  dissmissProgressBar(){
        mHandler.sendEmptyMessage(dissmissProgressBar);
    }

    public void showResultOnMap(Object param){
        webView.loadUrl("javascript:setAddress('" + param+ "')");
    }
}
