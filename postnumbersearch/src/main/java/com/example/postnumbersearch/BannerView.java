package com.example.postnumbersearch;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by lh on 2017/8/28.
 */
public class BannerView extends RelativeLayout implements View.OnClickListener{

    private RelativeLayout adContainer;
    private ImageView hideAd;
    private AdManager adManager;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUi(context);
    }

    private void initUi(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_banner, this);
        adContainer = (RelativeLayout) view.findViewById(R.id.ad_container);
        adManager = new AdManager();
        hideAd = (ImageView) view.findViewById(R.id.hide_ad);
        hideAd.setOnClickListener(this);
    }

    public void showBanner(Activity mContext, String SDK_BANNER_AD_ID ){
        adManager.showBanner(mContext,adContainer,SDK_BANNER_AD_ID );
    }

    public void hideBanner(){
        adManager.hideBanner(adContainer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hide_ad:
                hideAd.setVisibility(View.GONE);
                hideBanner();
        }
    }
}
