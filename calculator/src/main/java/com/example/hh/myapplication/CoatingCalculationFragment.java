package com.example.hh.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
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

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by hh on 2017/1/13.
 */
public class CoatingCalculationFragment extends BaseFragment implements View.OnClickListener{

    private EditText cocatingCoverEdit;
    private EditText cocatingPriceEdit;

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coating_calculation,container,false);
        return view;
    }

    @Override
    public void setTitleText() {
       titleText.setText("涂料计算");
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
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
        cocatingCoverEdit = (EditText) view.findViewById(R.id.cocating_cover_edit);
        cocatingPriceEdit = (EditText) view.findViewById(R.id.cocating_price_edit);
        totalCountResult = (TextView) view.findViewById(R.id.cocating_count_edit);
        totalPriceResult = (TextView) view.findViewById(R.id.cocating_total_price_edit);
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
        cocatingCoverEdit.addTextChangedListener(mTextWatcher);
        cocatingPriceEdit.addTextChangedListener(mTextWatcher);
    }

    public void fillData() {
//创建MySQLiteOpenHelper辅助类对象

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

        fillHoseData(info);

    }

    /**写入房间相关数据*/
    public void fillHoseData(HouseInfo info){
        if (info != null){
            if (info.getmPerPrice() != null)
                cocatingPriceEdit.setText(info.getmPerPrice());
            if (info.getmSize() != null)
                cocatingCoverEdit.setText(info.getmSize());
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
        cocatingPriceEdit.setText("");
        cocatingCoverEdit.setText("");

    }

    public void calculateData(){

        float houseArea,windowArea,doorArea;
        int coatingCountresult , coatingPriceresult ;
        if(TextUtils.isEmpty(houseHeight.getText()) || TextUtils.isEmpty(houseLong.getText()) || TextUtils.isEmpty(houseWidth.getText())){
            Toast.makeText(getActivity(),"房间信息不能为空哦",Toast.LENGTH_SHORT).show();
            return;
        }

        houseArea = getHouseArea();
        windowArea = getWindowArea();
        doorArea = getDoorArea();

        float cocatingCover = 0.0f;
        if(!TextUtils.isEmpty(cocatingCoverEdit.getText())){
            cocatingCover = Float.parseFloat(cocatingCoverEdit.getText().toString());
        }else {
            cocatingCover = Float.parseFloat(cocatingCoverEdit.getHint().toString());
        }
        resultLayout.setVisibility(View.VISIBLE);
        if(cocatingCover > 0){
            coatingCountresult  = (int)Math.ceil((houseArea - windowArea - doorArea)/cocatingCover);
            if(coatingCountresult <= 0){
                Toast.makeText(getActivity(),"墙的面积为负数啦，检查一下您输入的参数吧！", Toast.LENGTH_SHORT).show();
                return;
            }
            totalCountResult.setText(String.valueOf(coatingCountresult));
        }else{
            Toast.makeText(getActivity(),"请输入正确的涂料覆盖率", Toast.LENGTH_SHORT).show();
            return;
        }

        float cocatingPrice = 0.0f;
        if(!TextUtils.isEmpty(cocatingPriceEdit.getText())){
            cocatingPrice = Float.parseFloat(cocatingPriceEdit.getText().toString());
        }


        if(cocatingPrice > 0){
            coatingPriceresult  = (int)Math.ceil(coatingCountresult*cocatingPrice);
            totalPriceResult.setText(String.valueOf(coatingPriceresult));
        }else{
            Toast.makeText(getActivity(),"请输入正确的涂料单价", Toast.LENGTH_SHORT).show();
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
        historyInfo.setmName("涂料计算");
        historyInfo.setmResultCount(totalCountResult.getText().toString().trim());
        historyInfo.setmResultPrice(totalPriceResult.getText().toString().trim());
        historyInfo.setmTime(dateFormat.format(date));
        historyInfo.setmType(Constant.CaculatorType.COATING.name());
        historyInfo.setHouseLong(houseLong.getText().toString().trim());
        historyInfo.setHouseWidth(houseWidth.getText().toString().trim());
        historyInfo.setHouseHeight(houseHeight.getText().toString().trim());
        historyInfo.setDoorHeight(doorHeight.getText().toString().trim());
        historyInfo.setDoorCount(doorCount.getText().toString().trim());
        historyInfo.setDoorWidth(doorWidth.getText().toString().trim());
        historyInfo.setWindowCount(windowCount.getText().toString().trim());
        historyInfo.setWindowHeight(windowHeight.getText().toString().trim());
        historyInfo.setWindowWidth(windowWidth.getText().toString().trim());
        historyInfo.setmPerPrice(cocatingPriceEdit.getText().toString().trim());
        historyInfo.setmSize(cocatingCoverEdit.getText().toString().trim());
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
