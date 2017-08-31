package com.example.postnumbersearch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class OneKeySearchActivity extends BaseActivity implements View.OnClickListener{

    private TextView resultTextView;
    private HttpRequest httpRequest;
    private RecyclerView recyclerview;
    private AddressAdapter addressAdapter;
    private List<AddressInfo> addressInfoList = new ArrayList<AddressInfo>();
    private RelativeLayout progressBar1Layout;
    private InputMethodManager imm;
    private String city = "";
    private String district = "";
    private String province ="";
    private WebView webView;
    private WebSettings webSettings;
    private JSInterface jsInterface;
    private Button switchButton;
    private BannerView bannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onekey_search);
        initUi();
        showDate();
        requestData();
        initMap();
    }

    private void initMap() {
        // 地图初始化
        jsInterface = new JSInterface(webView,mHandler);
        webSettings = webView.getSettings();
        // 开启DomStorage缓存
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        // webview支持js脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setBlockNetworkImage(false);
        // 启用地理定位
        webSettings.setGeolocationEnabled(true);
        // 启用数据库
        webSettings.setDatabaseEnabled(true);
        // 设置定位的数据库路径
        String dir = this.getApplicationContext()
                .getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.setJavaScriptEnabled(true);
        Log.v("LH","before");
        webView.addJavascriptInterface(jsInterface,"ps");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });
        showProgressBar();
        webView.loadUrl("file:///android_asset/www/html/demo.html");

    }

    private void requestData() {
        if(!Util.isNetworkConnected(this)){
            Toast.makeText(this,getResourceText(R.string.network_ubreachable),Toast.LENGTH_SHORT).show();
        }
        httpRequest = new HttpRequest(OneKeySearchActivity.this,new OnHttpRequestListener() {
            @Override
            public void onStart() {
                showProgressBar();
            }

            @Override
            public void onSuccess(DataBean data) {
                addressInfoList.clear();
                addressInfoList.addAll( data.getResult());
                if(addressInfoList.size() == 0){
                    resultTextView.setText(getResourceText(R.string.no_search_info));
                    showNoSearchResult();
                }else{
                    addressAdapter.notifyDataSetChanged();
                    showSearchResult();
                }
                dismissProgressBar();
            }

            @Override
            public void onError(String errorMsg) {
                dismissProgressBar();
            }
        });
    }

    private void initUi() {
        resultTextView = (TextView) findViewById(R.id.result);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar1Layout = (RelativeLayout) findViewById(R.id.progressBar1_layout);
        webView = (WebView) findViewById(R.id.webView);
        switchButton = (Button) findViewById(R.id.switch_button);
        switchButton.setOnClickListener(this);
        webView.setOnClickListener(this);
        resultTextView.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
//        bannerView = (BannerView) findViewById(R.id.banner_view);
//        bannerView.showBanner(this,Cst.OneKeySearchBanner);

        showSearchResult();
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);

    }

    void onlyShowMap(){
        resultTextView.setVisibility(View.GONE);
        recyclerview.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        switchButton.setTag(Cst.exitMap);
        switchButton.setText(getResourceText(R.string.enter_search_info));
    }

    void showSearchResult(){
        webView.setVisibility(View.GONE);
        resultTextView.setVisibility(View.VISIBLE);
        recyclerview.setVisibility(View.VISIBLE);
        switchButton.setTag(Cst.enterMapTag);
        switchButton.setText(getResourceText(R.string.enter_map));
        if(city.isEmpty()){
            resultTextView.setText(getResourceText(R.string.get_location));
        }else{
            resultTextView.setText(getResourceText(R.string.current_locattion)+city);
        }

    }

    void showNoSearchResult(){
        webView.setVisibility(View.GONE);
        resultTextView.setVisibility(View.VISIBLE);
        recyclerview.setVisibility(View.GONE);
    }

    void showProgressBar(){
        progressBar1Layout.setVisibility(View.VISIBLE);
    }

    void dismissProgressBar(){
        progressBar1Layout.setVisibility(View.GONE);
    }

    private void showDate() {
        showSearchResult();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.getRecycledViewPool().setMaxRecycledViews(0, 6);
        recyclerview.setLayoutManager(layoutManager);
        addressAdapter = new AddressAdapter(this, addressInfoList);
        recyclerview.setAdapter(addressAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        // 还有下面这上三种动画FlipDownItemAnimator, SlideItemAnimator, FromTopItemAnimator
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case JSInterface.setLocationStr:
                    city = msg.obj.toString();
                    resultTextView.setText(getResourceText(R.string.current_locattion)+city);
                    httpRequest.requestNetData(msg.obj.toString());
                    break;
                case JSInterface.dissmissProgressBar:
                    dismissProgressBar();
                    break;
                case JSInterface.showProgressBar:
                    showProgressBar();
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.switch_button:
                if(switchButton.getTag().equals(Cst.enterMapTag)){
                    onlyShowMap();
                }else{
                    showSearchResult();
                }
                break;
        }
    }



}
