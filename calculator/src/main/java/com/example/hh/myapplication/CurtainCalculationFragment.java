package com.example.hh.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hh.myapplication.util.Constant;
import com.example.hh.myapplication.util.HistoryInfo;
import com.example.hh.myapplication.util.HouseInfo;

/**
 * 窗帘计算器
 * Created by hh on 2017/1/13.
 */
public class CurtainCalculationFragment extends BaseFragment implements View.OnClickListener{

    private EditText clothPriceEdit;
    private EditText clothWidthEdit;

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_curtain_calculation,container,false);
        return view;
    }

    public void initUi(View view) {

        windowHeight = (EditText)view.findViewById(R.id.window_height_edit);
        windowWidth = (EditText)view.findViewById(R.id.window_width_edit);
        windowCount = (EditText)view.findViewById(R.id.window_count_edit);
        resultLayout = (RelativeLayout)view.findViewById(R.id.result_layout);
        scrollView = (ScrollView)view. findViewById(R.id.scrollView);
        clothWidthEdit = (EditText) view.findViewById(R.id.cloth_width_edit);
        clothPriceEdit = (EditText) view.findViewById(R.id.cloth_price_edit);
        totalCountResult = (TextView) view.findViewById(R.id.cloth_count_edit);
        totalPriceResult = (TextView) view.findViewById(R.id.cloth_total_price_edit);

        addListener(view);
    }

    @Override
    public void setTitleText() {
        titleText.setText("窗帘计算");
    }

    private void addListener(View view) {
        save.setOnClickListener(this);
        view.findViewById(R.id.restart).setOnClickListener(this);
        view.findViewById(R.id.calculate).setOnClickListener(this);
        windowHeight.addTextChangedListener(mTextWatcher);
        windowWidth.addTextChangedListener(mTextWatcher);
        windowCount.addTextChangedListener(mTextWatcher);
        clothPriceEdit.addTextChangedListener(mTextWatcher);
        clothWidthEdit.addTextChangedListener(mTextWatcher);
    }

    public void fillData() {

        HouseInfo info;
        if(isFromHistory){
            historyList = myHelper.getHistoryList();
            info=historyList.get(position);

            if (!TextUtils.isEmpty(historyList.get(position).getmResultPrice())){
                resultLayout.setVisibility(View.VISIBLE);
                totalCountResult.setText(historyList.get(position).getmResultPrice());
                totalPriceResult.setText(historyList.get(position).getmResultCount());
            }else{
                resultLayout.setVisibility(View.GONE);
            }

        }else{
            info = myHelper.getHouseInfo();
        }
        if (info != null){
            if(info.getmPerPrice() != null)
                clothPriceEdit.setText(info.getmPerPrice());
            if(info.getmPerMaterialWidth() != null)
                clothWidthEdit.setText(info.getmPerMaterialWidth());
            if (info.getWindowHeight() != null)
                windowHeight.setText(info.getWindowHeight());
            if (info.getWindowWidth() != null)
                windowWidth.setText(info.getWindowWidth());
            if (info.getWindowCount() != null)
                windowCount.setText(info.getWindowCount());
        }
    }

    public void clearData(){
        super.clearData();
        resultLayout.setVisibility(View.GONE);
        clothWidthEdit.setText("");
        clothPriceEdit.setText("");
    }

    public void calculateData(){

        float windowArea;
        int countresult, priceresult;

        /**计算窗户面积*/
        float windowCountData,windowWidthData =0.0f,windowHeightData =0.0f;
        if(TextUtils.isEmpty(windowHeight.getText()) || TextUtils.isEmpty(windowWidth.getText())){
            windowArea = 0.0f;
        }else{
            windowHeightData = Float.parseFloat(windowHeight.getText().toString());
            windowWidthData = Float.parseFloat(windowWidth.getText().toString());
            windowCountData = Float.parseFloat(windowCount.getText().toString());
            windowArea = windowCountData*windowHeightData*windowWidthData;
        }

        float clothPriceData=0.0f,clothWidthData = 0.0f;
        if(!TextUtils.isEmpty(clothWidthEdit.getText())){
            clothWidthData = Float.parseFloat(clothWidthEdit.getText().toString());
        }else{
            Toast.makeText(getActivity(),"请输入正确的布料宽度", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!TextUtils.isEmpty(clothPriceEdit.getText())){
            clothPriceData = Float.parseFloat(clothPriceEdit.getText().toString());
        }else{
            Toast.makeText(getActivity(),"请输入正确的布料单价", Toast.LENGTH_SHORT).show();
            return;
        }
        resultLayout.setVisibility(View.VISIBLE);

        float c1 = Math.round((windowWidthData + 0.3)*2/windowWidthData);
        countresult = (int)Math.ceil(c1*(windowHeightData+0.4));
        totalCountResult.setText(String.valueOf(countresult));

        if(!TextUtils.isEmpty(clothPriceEdit.getText())){
            clothPriceData  = Float.parseFloat(clothPriceEdit.getText().toString());
        }
        if(clothPriceData > 0){
            priceresult = (int)Math.ceil(clothPriceData*countresult);
            totalPriceResult.setText(String.valueOf(priceresult));
        }else{
            Toast.makeText(getActivity(),"请输入正确的窗帘单价", Toast.LENGTH_SHORT).show();
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    /**保存历史记录*/
    public void saveRecord() {
        HistoryInfo historyInfo = new HistoryInfo();
        historyInfo.setmName("窗帘计算");
        historyInfo.setmTime(dateFormat.format(date));
        historyInfo.setmType(Constant.CaculatorType.CURTAIN.name());
        historyInfo.setmResultCount(totalCountResult.getText().toString().trim());
        historyInfo.setmResultPrice(totalPriceResult.getText().toString().trim());
        historyInfo.setWindowCount(windowCount.getText().toString().trim());
        historyInfo.setWindowHeight(windowHeight.getText().toString().trim());
        historyInfo.setWindowWidth(windowWidth.getText().toString().trim());
        historyInfo.setmPerPrice(clothPriceEdit.getText().toString().trim());
        historyInfo.setmPerMaterialWidth(clothWidthEdit.getText().toString().trim());

        myHelper.insertAndUpdateData(this.getActivity(),historyInfo,Constant.historyTableName);
        sendBroadCast(Constant.SHOW_HISTORY);

    }

    @Override
    public  void onClick(View v){
        switch (v.getId()){
            case R.id.restart:
                clearData();
                break;
            case R.id.calculate:
                calculateData();
                break;
            case R.id.save:
                saveRecord();
                break;
        }

    }
}
