package com.example.hh.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hh.myapplication.util.Constant;
import com.example.hh.myapplication.util.HistoryInfo;

import java.util.List;

/**
 * Created by hh on 2017/1/13.
 */
public class HistoryFragment extends Fragment implements View.OnClickListener{
    private RecyclerView recyclerview;
    private TextView empty;
    private List<HistoryInfo> historyList;
    private HistoryAdapter historyAdapter;
    private HistoryChangeReceiver mHistoryChangeReceiver;

    MySQLiteHelper myHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,container,false);
        initUI(view);


        myHelper = MySQLiteHelper.getDbHelper(this.getActivity());
        showDate();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHistoryChangeReceiver = new HistoryChangeReceiver();
        getActivity().registerReceiver(mHistoryChangeReceiver,new IntentFilter(Constant.SHOW_HISTORY));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mHistoryChangeReceiver);
    }

    private void initUI(View view) {
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        empty = (TextView) view.findViewById(R.id.empty);
    }

    private void showDate() {
        historyList = myHelper.getHistoryList();

        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // 设置垂直布局，目前仅支持LinearLayout(有垂直和横向)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置缓冲池最大循环使用view数
        recyclerview.getRecycledViewPool().setMaxRecycledViews(0, 6);
        // 设置布局管理器
        recyclerview.setLayoutManager(layoutManager);
        // 创建Adapter，并指定数据集
        historyAdapter = new HistoryAdapter(getActivity(), historyList);
        // 设置Adapter
        recyclerview.setAdapter(historyAdapter);
        // 设置默认动画
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        // 还有下面这上三种动画FlipDownItemAnimator, SlideItemAnimator, FromTopItemAnimator

        if (historyList.size() == 0) {
            empty.setVisibility(View.VISIBLE);
        }else {
            empty.setVisibility(View.GONE);
        }
    }

    @Override
    public  void onClick(View v){

    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            showDate();
        }
    }

    class HistoryChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            historyList.clear();
            historyList.addAll(myHelper.getHistoryList());
            historyAdapter.notifyDataSetChanged();
            if (historyList.size() == 0) {
                empty.setVisibility(View.VISIBLE);
            }else {
                empty.setVisibility(View.GONE);
            }
        }
        }

}
