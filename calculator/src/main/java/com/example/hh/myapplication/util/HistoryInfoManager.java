package com.example.hh.myapplication.util;

import java.util.List;

/**
 * Created by hh on 2017/2/14.
 */
public class HistoryInfoManager {
    private static HistoryInfoManager mHistoryInfoManager;
    private List<HistoryInfo> historyInfoList;

    public static HistoryInfoManager getInstance(){
        if(mHistoryInfoManager == null){
            mHistoryInfoManager = new HistoryInfoManager();
        }
        return mHistoryInfoManager;
    }

    public List<HistoryInfo> getHistoryInfoList() {
        return historyInfoList;
    }

    public void setHistoryInfoList(List<HistoryInfo> historyInfoList) {
        this.historyInfoList = historyInfoList;
    }
}
