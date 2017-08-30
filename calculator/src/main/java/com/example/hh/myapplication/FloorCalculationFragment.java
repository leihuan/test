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
 * 地板计算器
 * Created by hh on 2017/1/13.
 */
public class FloorCalculationFragment extends BaseFragment implements View.OnClickListener{

    private EditText floorHeightEdit;
    private EditText floorWidthEdit;
    private EditText perFloorPriceEdit;


    @Override
    public void setTitleText() {
        titleText.setText("地板计算");
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_floor_calculation,container,false);
    }

    public void initUi(View view) {
        houseLong = (EditText)view.findViewById(R.id.house_long_edittext);
        houseWidth = (EditText)view.findViewById(R.id.house_width_edittext);
        floorHeightEdit = (EditText)view.findViewById(R.id.floor_height_edit);
        floorWidthEdit = (EditText)view.findViewById(R.id.floor_width_edit);
        totalCountResult = (TextView) view.findViewById(R.id.floor_count_edit);
        totalPriceResult = (TextView) view.findViewById(R.id.floor_total_price_edit);
        perFloorPriceEdit = (EditText) view.findViewById(R.id.per_floor_price_edit);
        resultLayout = (RelativeLayout)view.findViewById(R.id.result_layout);
        scrollView = (ScrollView)view. findViewById(R.id.scrollView);
        addListener(view);
    }

    private void addListener(View view) {
        save.setOnClickListener(this);
        view.findViewById(R.id.restart).setOnClickListener(this);
        view.findViewById(R.id.calculate).setOnClickListener(this);
        houseLong.addTextChangedListener(mTextWatcher);
        houseWidth.addTextChangedListener(mTextWatcher);
        floorHeightEdit.addTextChangedListener(mTextWatcher);
        floorWidthEdit.addTextChangedListener(mTextWatcher);
        perFloorPriceEdit.addTextChangedListener(mTextWatcher);
    }


    public void fillData() {

        myHelper = new MySQLiteHelper(this.getActivity(), "my.db", null, 1);
        HouseInfo info;
        if(isFromHistory){
            historyList = myHelper.getHistoryList();
            info=historyList.get(position);

            if (!TextUtils.isEmpty(historyList.get(position).getmResultPrice())){
                resultLayout.setVisibility(View.VISIBLE);
                totalPriceResult.setText(historyList.get(position).getmResultPrice());
                totalCountResult.setText(historyList.get(position).getmResultCount());
            }else{
                resultLayout.setVisibility(View.GONE);
            }

        }else{
            info = myHelper.getHouseInfo();
        }
        if (info != null){
            if(info.getmPerPrice() != null)
                perFloorPriceEdit.setText(info.getmPerPrice());
            if(info.getmPerMaterialLong() != null)
                floorHeightEdit.setText(info.getmPerMaterialLong());
            if(info.getmPerMaterialWidth() != null)
                floorWidthEdit.setText(info.getmPerMaterialWidth());
            if (info.getHouseLong() != null)
                houseLong.setText(info.getHouseLong());
            if (info.getHouseWidth() != null)
                houseWidth.setText(info.getHouseWidth());
        }

    }

    public void clearData(){
        super.clearData();
        floorHeightEdit.setText("");
        floorWidthEdit.setText("");
        perFloorPriceEdit.setText("");
    }

    public void calculateData(){

        float houseArea,perFloorArea,perFloorPrice=0.0f;
        int floorPriceresult = 0;
        int  floorgCountresult = 0;
        if(TextUtils.isEmpty(houseLong.getText()) || TextUtils.isEmpty(houseWidth.getText())){
            Toast.makeText(getActivity(),"房间信息不能为空哦",Toast.LENGTH_SHORT).show();
            return;
        }
        /**计算房屋面积*/
        float houseLongData,houseWidthData;
        houseWidthData = Float.parseFloat(houseWidth.getText().toString());
        houseLongData = Float.parseFloat(houseLong.getText().toString());
        houseArea =houseWidthData*houseLongData;

        /**计算单块地板面积*/
        float floorLongData,floorWidthData;
        if(TextUtils.isEmpty(floorWidthEdit.getText()) || TextUtils.isEmpty(floorHeightEdit.getText())){
            Toast.makeText(getActivity(),"地板信息不能为空哦",Toast.LENGTH_SHORT).show();
            return;
        }
        floorWidthData = Float.parseFloat(floorWidthEdit.getText().toString());
        floorLongData = Float.parseFloat(floorHeightEdit.getText().toString());
        perFloorArea =floorWidthData*floorLongData*0.0001f;

        floorgCountresult = (int)Math.ceil((houseArea/perFloorArea));
        totalCountResult.setText(String.valueOf(floorgCountresult));
        if(!TextUtils.isEmpty(perFloorPriceEdit.getText())){
            perFloorPrice = Float.parseFloat(perFloorPriceEdit.getText().toString());
        }
        if(perFloorPrice > 0){
            floorPriceresult  = (int)Math.ceil(floorgCountresult*perFloorPrice);
            totalPriceResult.setText(String.valueOf(floorPriceresult));
        }else{
            Toast.makeText(getActivity(),"请输入正确的地板单价", Toast.LENGTH_SHORT).show();
            return;
        }
        resultLayout.setVisibility(View.VISIBLE);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }


    public void saveRecord() {

        HistoryInfo historyInfo = new HistoryInfo();
        historyInfo.setmName("地板计算");
        historyInfo.setmResultCount(totalCountResult.getText().toString().trim());
        historyInfo.setmResultPrice(totalPriceResult.getText().toString().trim());
        historyInfo.setmTime(dateFormat.format(date));
        historyInfo.setmType(Constant.CaculatorType.FLOOR.name());
        historyInfo.setHouseLong(houseLong.getText().toString().trim());
        historyInfo.setHouseWidth(houseWidth.getText().toString().trim());
        historyInfo.setmPerPrice(perFloorPriceEdit.getText().toString().trim());
        historyInfo.setmPerMaterialLong(floorHeightEdit.getText().toString().trim());
        historyInfo.setmPerMaterialWidth(floorWidthEdit.getText().toString().trim());

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
