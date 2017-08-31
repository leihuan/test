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
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class PostnumberSearchActivity extends BaseActivity implements View.OnClickListener{

    private Button search;
    private TextView resultTextView;
    private SearchView searchView;
    private HttpRequest httpRequest;
    private RecyclerView recyclerview;
    private AddressAdapter addressAdapter;
    private List<AddressInfo> addressInfoList = new ArrayList<AddressInfo>();
    private RelativeLayout progressBar1Layout;
    private InputMethodManager imm;
    private WebView webView;
    private WebSettings webSettings;
    private JSInterface jsInterface;
    private Button switchButton;
    private String city = "";
    private String district = "";
    private String province ="";
    private BannerView bannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postnumber_search);
        initUi();
        showDate();
        requestData();
        initMap();
    }


    private void requestData() {
        if(!Util.isNetworkConnected(this)){
            Toast.makeText(this,getResourceText(R.string.network_ubreachable),Toast.LENGTH_SHORT).show();
        }
        httpRequest = new HttpRequest(PostnumberSearchActivity.this,new OnHttpRequestListener() {
            @Override
            public void onStart() {
                showProgressBar();
            }

            @Override
            public void onSuccess(DataBean data) {
                addressInfoList.clear();
                addressInfoList.addAll( data.getResult());
                if(addressInfoList.size() == 0){
                    showNoSearchResult();
                }else{
                    addressAdapter.notifyDataSetChanged();
                    province = addressInfoList.get(0).getProvince();
                    city = addressInfoList.get(0).getCity();
                    district = addressInfoList.get(0).getDistrict();
                    if(!TextUtils.isEmpty(district)){
                        jsInterface.showResultOnMap(district);
                    }else if(!TextUtils.isEmpty(city)){
                        jsInterface.showResultOnMap(city);
                    }else{
                        jsInterface.showResultOnMap(province);
                    }
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
        search = (Button) findViewById(R.id.search);
        resultTextView = (TextView) findViewById(R.id.result);
        searchView = (SearchView) findViewById(R.id.searchView);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar1Layout = (RelativeLayout) findViewById(R.id.progressBar1_layout);
        search.setOnClickListener(this);
        resultTextView.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        switchButton = (Button) findViewById(R.id.switch_button);
        switchButton.setOnClickListener(this);
        switchButton.setVisibility(View.GONE);
        webView = (WebView) findViewById(R.id.webView);
        webView.setOnClickListener(this);
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
//        bannerView = (BannerView) findViewById(R.id.banner_view);
//        bannerView.showBanner(this,Cst.PostNumberSearchBanner);

        searchView.setOnSearchClickListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initMap() {
        // 地图初始化
        jsInterface = new JSInterface(webView,null);
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
                // TODO Auto-generated method stub
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });
        webView.loadUrl("file:///android_asset/www/html/demo.html");

    }

    void onlyShowMap(){
        resultTextView.setVisibility(View.GONE);
        recyclerview.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        switchButton.setVisibility(View.VISIBLE);
        switchButton.setTag(Cst.exitMap);
        switchButton.setText(getResourceText(R.string.enter_search_info));
    }

    void showSearchResult(){
        webView.setVisibility(View.GONE);
        resultTextView.setVisibility(View.VISIBLE);
        recyclerview.setVisibility(View.VISIBLE);
        switchButton.setTag(Cst.enterMapTag);
        switchButton.setText(getResourceText(R.string.enter_map));
        switchButton.setVisibility(View.VISIBLE);
        resultTextView.setText(getResources().getText(R.string.search_info));
    }

    void showNoSearchResult(){
        webView.setVisibility(View.GONE);
        resultTextView.setVisibility(View.VISIBLE);
        recyclerview.setVisibility(View.GONE);
        switchButton.setVisibility(View.VISIBLE);
        resultTextView.setText(getResources().getText(R.string.no_search_info));
        switchButton.setVisibility(View.GONE);
    }

    void showProgressBar(){
        progressBar1Layout.setVisibility(View.VISIBLE);
    }

    void dismissProgressBar(){
        progressBar1Layout.setVisibility(View.GONE);
    }

    private void showDate() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.getRecycledViewPool().setMaxRecycledViews(0, 6);
        recyclerview.setLayoutManager(layoutManager);
        addressAdapter = new AddressAdapter(this, addressInfoList);
        recyclerview.setAdapter(addressAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        // 还有下面这上三种动画FlipDownItemAnimator, SlideItemAnimator, FromTopItemAnimator

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search:
                httpRequest.requestNetData(searchView.getQuery().toString());
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                break;

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
