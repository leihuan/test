package com.example.hh.myapplication.util;

import java.io.Serializable;

/**
 * Created by hh on 2017/2/13.
 */
public class HistoryInfo extends HouseInfo implements Serializable{

    public String id;
    public String mName;
    public String mTime;
    /**总数量*/
    public String mResultCount;
    /**总价*/
    public String mResultPrice;
    public String mType;




    public String getId() {
        return id;
    }

    public String getmName() {
        return mName;
    }

    public String getmTime() {
        return mTime;
    }
    public String getmResultCount() {
        return mResultCount;
    }

    public String getmResultPrice() {
        return mResultPrice;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public void setmResultCount(String mResultCount) {
        this.mResultCount = mResultCount;
    }

    public void setmResultPrice(String mResultPrice) {
        this.mResultPrice = mResultPrice;
    }

    public String getmType() {
        return mType;
    }
}
