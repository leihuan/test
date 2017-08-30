package com.example.hh.myapplication;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.hh.myapplication.util.HistoryInfo;
import com.example.hh.myapplication.util.MyTextWatcher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lh on 2017/3/7.
 */
public abstract class  BaseFragment extends Fragment{

    EditText houseLong,houseWidth,houseHeight,doorHeight,doorWidth,doorCount,windowHeight,windowWidth,windowCount;
    TextView totalCountResult;
    TextView totalPriceResult;
    TextView titleText;
    TextView save;
    RelativeLayout resultLayout;
    ScrollView scrollView;
    List<HistoryInfo> historyList;
    MySQLiteHelper myHelper;
    MyTextWatcher mTextWatcher = new MyTextWatcher();
    SimpleDateFormat dateFormat;
    Date date;
    Handler mHandler = new Handler();

    int position;
    boolean isFromHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isFromHistory = getActivity().getIntent().getBooleanExtra("from",false);
        position = getActivity().getIntent().getIntExtra("position",0);
        View view = getView(inflater, container, savedInstanceState);
        initParam(view);
        initUi(view);
        fillData();
        return view;
    }

    private void initParam(View view) {
        myHelper = new MySQLiteHelper(this.getActivity(), "my.db", null, 1);
        Calendar calendar = Calendar.getInstance();
        date = calendar.getTime();
        dateFormat= new SimpleDateFormat("yy/MM/dd HH:mm");

        titleText = (TextView) view.findViewById(R.id.title_text);
        save = (TextView) view.findViewById(R.id.save);
        scrollView = (ScrollView)view. findViewById(R.id.scrollView);
        resultLayout = (RelativeLayout)view.findViewById(R.id.result_layout);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        setTitleText();
    }

    public void sendBroadCast(String action){
        Intent intent = new  Intent();
        intent.setAction(action);
        getActivity().sendBroadcast(intent);
    }

    /**计算涂料面积*/
    public float getHouseArea(){
        float houseLongData,houseWidthData,houseHeightData,houseArea;
        houseHeightData = Float.parseFloat(houseHeight.getText().toString());
        houseWidthData = Float.parseFloat(houseWidth.getText().toString());
        houseLongData = Float.parseFloat(houseLong.getText().toString());
//        houseArea = houseHeightData*houseWidthData*houseLongData*4;
        houseArea = (houseLongData+houseWidthData)*2*houseHeightData+houseWidthData*houseLongData;
        return houseArea;
    }

    public float getWallArea(){
        float houseLongData,houseWidthData,houseHeightData,wallArea;
        houseHeightData = Float.parseFloat(houseHeight.getText().toString());
        houseWidthData = Float.parseFloat(houseWidth.getText().toString());
        houseLongData = Float.parseFloat(houseLong.getText().toString());
//        houseArea = houseHeightData*houseWidthData*houseLongData*4;
        wallArea = (houseLongData+houseWidthData)*2*houseHeightData;
        return wallArea;
    }

    public float getWindowArea(){
        float windowCountData,windowWidthData,windowHeightData,windowArea;
        if(TextUtils.isEmpty(windowHeight.getText()) || TextUtils.isEmpty(windowWidth.getText())){
            windowArea = 0.0f;
        }else{
            windowHeightData = Float.parseFloat(windowHeight.getText().toString());
            windowWidthData = Float.parseFloat(windowWidth.getText().toString());
            windowCountData = Float.parseFloat(windowCount.getText().toString());
            windowArea = windowCountData*windowHeightData*windowWidthData;
        }
        return windowArea;
    }

    public float getDoorArea(){
        float doorCountData,doorWidthData,doorHeightData,doorArea;
        if(TextUtils.isEmpty(doorHeight.getText()) || TextUtils.isEmpty(doorWidth.getText())){
            doorArea = 0.0f;
        }else{
            doorHeightData = Float.parseFloat(doorHeight.getText().toString());
            doorWidthData = Float.parseFloat(doorWidth.getText().toString());
            doorCountData = Float.parseFloat(doorCount.getText().toString());
            doorArea = doorHeightData*doorWidthData*doorCountData;
        }
        return doorArea;
    }

    public abstract void setTitleText();

    public abstract View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void calculateData();

    public abstract void saveRecord();

    public  void clearData(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        resultLayout.setVisibility(View.GONE);
        totalCountResult.setText("");
        totalPriceResult.setText("");
        if(houseLong != null)
            houseLong.setText("");
        if(houseWidth != null)
            houseWidth.setText("");
        if(houseHeight != null)
            houseHeight.setText("");
        if(doorHeight != null)
            doorHeight.setText("");
        if(doorWidth != null)
            doorWidth.setText("");
        if(doorCount != null)
            doorCount.setText("");
        if(windowHeight != null)
            windowHeight.setText("");
        if(windowWidth != null)
            windowWidth.setText("");
        if(windowCount != null)
            windowCount.setText("");
    }

    public abstract void fillData();

    public abstract void initUi(View view);

}
