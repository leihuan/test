package com.example.postnumbersearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.appx.BDBannerAd;
import com.baidu.appx.BDInterstitialAd;
import com.baidu.appx.BDSplashAd;


public class MainActivity extends BaseActivity implements View.OnClickListener {


    private TextView title;
    private Button adr;
    private Button postNumber;
    private Button oneKeySearch;
    private long exitTime = 0;
    private BannerView bannerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
    }

    private void initUi() {
        title = (TextView) findViewById(R.id.title);
        adr = (Button) findViewById(R.id.adr);
        postNumber = (Button) findViewById(R.id.post_number);
        oneKeySearch = (Button) findViewById(R.id.one_key_search);
//        bannerView = (BannerView) findViewById(R.id.banner_view);
//        bannerView.showBanner(this,Cst.mainPageBanner);
        adr.setOnClickListener(this);
        postNumber.setOnClickListener(this);
        oneKeySearch.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.adr:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddressSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.post_number:
                Intent intentPostNumber = new Intent();
                intentPostNumber.setClass(MainActivity.this, PostnumberSearchActivity.class);
                startActivity(intentPostNumber);
                break;
            case R.id.one_key_search:
                Intent intentOneKey = new Intent();
                intentOneKey.setClass(MainActivity.this, OneKeySearchActivity.class);
                startActivity(intentOneKey);
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
