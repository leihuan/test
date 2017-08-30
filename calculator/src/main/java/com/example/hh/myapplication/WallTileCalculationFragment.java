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
 * 墙砖计算器
 * Created by hh on 2017/1/13.
 */
public class WallTileCalculationFragment extends BaseFragment implements View.OnClickListener{

    private EditText perWallTilePriceEdit;
    private EditText wallTileHeightEdit;
    private EditText wallTileWidthEdit;

    @Override
    public void setTitleText() {
        titleText.setText("墙砖计算");
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wall_tile_calculation,container,false);
    }

    public void initUi(View view) {
        houseLong = (EditText)view.findViewById(R.id.house_long_edittext);
        houseWidth = (EditText)view.findViewById(R.id.house_width_edittext);
        houseHeight = (EditText)view.findViewById(R.id.house_height_edittext);
        doorHeight = (EditText)view.findViewById(R.id.door_height_edit);
        doorWidth = (EditText)view.findViewById(R.id.door_width_edit);
        doorCount = (EditText)view.findViewById(R.id.door_count_edit);
        windowHeight = (EditText)view.findViewById(R.id.window_height_edit);
        windowWidth = (EditText)view.findViewById(R.id.window_width_edit);
        windowCount = (EditText)view.findViewById(R.id.window_count_edit);
        totalCountResult = (TextView) view.findViewById(R.id.wall_tile_count_edit);
        totalPriceResult = (TextView) view.findViewById(R.id.wall_tile_total_price_edit);
        perWallTilePriceEdit = (EditText) view.findViewById(R.id.per_wall_tile_price_edit);
        wallTileHeightEdit = (EditText) view.findViewById(R.id.wall_tile_height_edit);
        wallTileWidthEdit = (EditText) view.findViewById(R.id.wall_tile_width_edit);
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
        houseHeight.addTextChangedListener(mTextWatcher);
        doorHeight.addTextChangedListener(mTextWatcher);
        doorWidth.addTextChangedListener(mTextWatcher);
        doorCount.addTextChangedListener(mTextWatcher);
        windowHeight.addTextChangedListener(mTextWatcher);
        windowWidth.addTextChangedListener(mTextWatcher);
        windowCount.addTextChangedListener(mTextWatcher);
        perWallTilePriceEdit.addTextChangedListener(mTextWatcher);
        wallTileHeightEdit.addTextChangedListener(mTextWatcher);
        wallTileWidthEdit.addTextChangedListener(mTextWatcher);
    }

    public void fillData() {
//创建MySQLiteOpenHelper辅助类对象
        myHelper = new MySQLiteHelper(this.getActivity(), "my.db", null, 1);
        HouseInfo info;
        if(isFromHistory){
            historyList = myHelper.getHistoryList();
            info=historyList.get(position);
            perWallTilePriceEdit.setText(historyList.get(position).getmPerPrice());
            wallTileHeightEdit.setText(historyList.get(position).getmPerMaterialLong());
            wallTileWidthEdit.setText(historyList.get(position).getmPerMaterialWidth());
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
        fillHoseData(info);

    }

    /**写入房间相关数据*/
    public void fillHoseData(HouseInfo info){
        if (info != null){
            if (info.getHouseLong() != null)
                houseLong.setText(info.getHouseLong());
            if (info.getHouseWidth() != null)
                houseWidth.setText(info.getHouseWidth());
            if (info.getHouseHeight() != null)
                houseHeight.setText(info.getHouseHeight());
            if (info.getDoorHeight() != null)
                doorHeight.setText(info.getDoorHeight());
            if (info.getDoorWidth() != null)
                doorWidth.setText(info.getDoorWidth());
            if (info.getDoorCount() != null)
                doorCount.setText(info.getDoorCount());
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
        wallTileHeightEdit.setText("");
        wallTileWidthEdit.setText("");
        perWallTilePriceEdit.setText("");
    }

    public void calculateData(){

        float wallArea,windowArea,doorArea,perFloorArea;
        int wallTileCountresult;
        int wallTilePriceresult;
        if(TextUtils.isEmpty(houseHeight.getText()) || TextUtils.isEmpty(houseLong.getText()) || TextUtils.isEmpty(houseWidth.getText())){
            Toast.makeText(getActivity(),"房间信息不能为空哦",Toast.LENGTH_SHORT).show();
            return;
        }
        wallArea = getWallArea();
        windowArea = getWindowArea();
        doorArea = getDoorArea();
        resultLayout.setVisibility(View.VISIBLE);
        /**计算单块墙砖面积*/
        float wallTileLongData,wallTileWidthData,perWallTilePrice=0.0f;
        if(TextUtils.isEmpty(wallTileWidthEdit.getText()) || TextUtils.isEmpty(wallTileHeightEdit.getText())){
            Toast.makeText(getActivity(),"墙砖信息不能为空哦",Toast.LENGTH_SHORT).show();
            return;
        }
        wallTileLongData = Float.parseFloat(wallTileWidthEdit.getText().toString());
        wallTileWidthData = Float.parseFloat(wallTileHeightEdit.getText().toString());
        perFloorArea =wallTileLongData*wallTileWidthData*0.000001f;

        wallTileCountresult = (int) Math.ceil((wallArea-doorArea-windowArea)/perFloorArea*1.05);
        totalCountResult.setText(String.valueOf(wallTileCountresult));
        if(!TextUtils.isEmpty(perWallTilePriceEdit.getText())){
            perWallTilePrice = Float.parseFloat(perWallTilePriceEdit.getText().toString());
        }
        if(perWallTilePrice > 0){
            wallTilePriceresult  = (int)Math.ceil(perWallTilePrice*wallTileCountresult);
            totalPriceResult.setText(String.valueOf(wallTilePriceresult));
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

    /**保存历史记录*/
    public void saveRecord() {
        HistoryInfo historyInfo = new HistoryInfo();
        historyInfo.setmName("墙砖计算");
        historyInfo.setmResultCount(totalCountResult.getText().toString().trim());
        historyInfo.setmResultPrice(totalPriceResult.getText().toString().trim());
        historyInfo.setmTime(dateFormat.format(date));
        historyInfo.setmType(Constant.CaculatorType.WALL.name());
        historyInfo.setHouseLong(houseLong.getText().toString().trim());
        historyInfo.setHouseWidth(houseWidth.getText().toString().trim());
        historyInfo.setHouseHeight(houseHeight.getText().toString().trim());
        historyInfo.setDoorHeight(doorHeight.getText().toString().trim());
        historyInfo.setDoorCount(doorCount.getText().toString().trim());
        historyInfo.setDoorWidth(doorWidth.getText().toString().trim());
        historyInfo.setWindowCount(windowCount.getText().toString().trim());
        historyInfo.setWindowHeight(windowHeight.getText().toString().trim());
        historyInfo.setWindowWidth(windowWidth.getText().toString().trim());
        historyInfo.setmPerPrice(perWallTilePriceEdit.getText().toString().trim());
        historyInfo.setmPerMaterialLong(wallTileHeightEdit.getText().toString().trim());
        historyInfo.setmPerMaterialWidth(wallTileWidthEdit.getText().toString().trim());

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
