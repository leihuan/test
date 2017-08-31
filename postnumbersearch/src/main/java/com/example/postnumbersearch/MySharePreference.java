package com.example.postnumbersearch;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lh on 2017/8/14.
 */
public class MySharePreference {

    private SharedPreferences mSharePreferences;
    private Context mContext;

    public MySharePreference(Context mContext){
        this.mContext = mContext;
    }

    public SharedPreferences getmSharePreferences() {
        mSharePreferences = mContext.getSharedPreferences("search_history", 0);
        return mSharePreferences;
    }
    public String getValue(String key){
        return getmSharePreferences().getString(key,"");
    }

    public void putValue(String key ,String value){
        SharedPreferences.Editor editor = getmSharePreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }
}
