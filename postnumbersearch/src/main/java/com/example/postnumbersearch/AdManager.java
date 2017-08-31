package com.example.postnumbersearch;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.appx.BDBannerAd;
import com.baidu.appx.BDInterstitialAd;
import com.baidu.appx.BDSplashAd;

/**
 * Created by lh on 2017/8/28.
 */
public class AdManager {

    private BDBannerAd bannerview;
    private String SDK_APP_KEY = Cst.AdAppId;
    private Activity mContext;
//    private static AdManager adManager = null;

//    public static AdManager getInstance(){
//        synchronized (AdManager.class){
//            if (null == adManager){
//                adManager = new AdManager();
//            }
//            return adManager;
//        }
//    }
    public void showBanner(Activity mContext, ViewGroup container, String SDK_BANNER_AD_ID ) {
        this.mContext = mContext;
        if (null == bannerview) {
            bannerview = new BDBannerAd(mContext, SDK_APP_KEY, SDK_BANNER_AD_ID);
            bannerview.setAdSize(BDBannerAd.SIZE_FLEXIBLE);
            bannerview.setAdListener(new AdListener("Banner"));
            container.addView(bannerview);
        }
        else {
            Log.v("Banner","---- bannerAd is showing, should hide first");
        }
    }

    public void hideBanner(ViewGroup container) {
        if (bannerview != null) {
            container.removeAllViews();
            bannerview.destroy();
            bannerview = null;
            Log.v("LH","---- bannerAd is hidden ----");
        }
        else {
            Log.v("LH","---- bannerAd not found ----");
        }
    }

    private class AdListener implements  BDBannerAd.BannerAdListener, BDInterstitialAd.InterstitialAdListener, BDSplashAd.SplashAdListener {
        private String stringTag;
        public AdListener(String tag) {
            this.stringTag = tag;
        }

        @Override
        public void onAdvertisementDataDidLoadFailure() {
            Log.v(stringTag, "    ad did load failure");
        }

        @Override
        public void onAdvertisementDataDidLoadSuccess() {
            Log.v(stringTag, "    ad did load success");
        }

        @Override
        public void onAdvertisementViewDidClick() {
            Log.v(stringTag, "    ad view did click");
        }

        @Override
        public void onAdvertisementViewDidShow() {
            Log.v(stringTag, "    ad view did show");
        }

        @Override
        public void onAdvertisementViewWillStartNewIntent() {
            Log.v(stringTag, "    ad view will new intent");
        }

        @Override
        public void onAdvertisementViewDidHide() {
            Log.v(stringTag, "    ad view did hide");
        }

    }
}
