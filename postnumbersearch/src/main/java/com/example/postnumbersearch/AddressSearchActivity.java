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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class AddressSearchActivity extends BaseActivity implements View.OnClickListener{

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
    private TextView progressText;
    private BannerView bannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_search);
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
                // TODO Auto-generated method stub
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });
        showProgressBar(getResourceText(R.string.loading_page));
        webView.loadUrl("file:///android_asset/www/html/demo_address_search.html");

    }

    private void requestData() {
        if(!Util.isNetworkConnected(this)){
            Toast.makeText(this,getResourceText(R.string.network_ubreachable),Toast.LENGTH_SHORT).show();
        }
        httpRequest = new HttpRequest(AddressSearchActivity.this,new OnHttpRequestListener() {
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
                    province = addressInfoList.get(0).getProvince();
                    city = addressInfoList.get(0).getCity();
                    district = addressInfoList.get(0).getDistrict();
                    addressAdapter.notifyDataSetChanged();
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
//                Toast.makeText(MainActivity.this,errorMsg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUi() {
        resultTextView = (TextView) findViewById(R.id.result);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar1Layout = (RelativeLayout) findViewById(R.id.progressBar1_layout);
        progressText = (TextView) findViewById(R.id.progressText);
        switchButton = (Button) findViewById(R.id.switch_button);
        switchButton.setOnClickListener(this);
        resultTextView.setOnClickListener(this);
        webView = (WebView) findViewById(R.id.webView);
        webView.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        onlyShowMap();
        switchButton.setVisibility(View.GONE);
//        bannerView = (BannerView) findViewById(R.id.banner_view);
//        bannerView.showBanner(this,Cst.AddressSearchBanner);
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
        resultTextView.setText(getResourceText(R.string.search_info));
    }

    void showNoSearchResult(){
        webView.setVisibility(View.GONE);
        resultTextView.setVisibility(View.VISIBLE);
        recyclerview.setVisibility(View.GONE);
        switchButton.setVisibility(View.VISIBLE);
        resultTextView.setText(getResourceText(R.string.search_info));
        switchButton.setVisibility(View.GONE);
    }

    void showProgressBar(){
        progressBar1Layout.setVisibility(View.VISIBLE);
    }

    void showProgressBar(String str){
        progressText.setText(str);
        progressBar1Layout.setVisibility(View.VISIBLE);
    }

    void dismissProgressBar(){
        progressBar1Layout.setVisibility(View.GONE);
    }

    private void showDate() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置缓冲池最大循环使用view数
        recyclerview.getRecycledViewPool().setMaxRecycledViews(0, 6);
        recyclerview.setLayoutManager(layoutManager);
        addressAdapter = new AddressAdapter(this, addressInfoList);
        recyclerview.setAdapter(addressAdapter);
//         设置默认动画
        recyclerview.setItemAnimator(new DefaultItemAnimator());

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //定位地址查询
                case JSInterface.setLocationStr:
//                    resultTextView.setText("您的当前位置，邮政编码为：");
                    httpRequest.requestNetData(msg.obj.toString());
                    break;
//                //搜索地址查询
                case JSInterface.setLocationArray:
                    String[] address = (String[]) msg.obj;
                    String str = "";
                    //针对直辖市
                    if(address[0].equals(address[1])){
                        str = address[1]+address[2];
                    }else{
                        str = address[0]+address[1]+address[2];
                    }
                    if(str.isEmpty()){
                        Toast.makeText(AddressSearchActivity.this,"查询信息不能为空",Toast.LENGTH_SHORT).show();
                    }else{
                        httpRequest.requestNetData(str);
                    }
                    break;
                case JSInterface.dissmissProgressBar:
                    dismissProgressBar();
                    break;
                case JSInterface.showProgressBar:
                    showProgressBar(getResourceText(R.string.loading_page));
                    break;

            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search:
//                resultTextView.setText("搜索结果：");
//                httpRequest.requestNetData(searchView.getQuery().toString());
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                break;
            case R.id.back:
                finish();
            case R.id.result:
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
