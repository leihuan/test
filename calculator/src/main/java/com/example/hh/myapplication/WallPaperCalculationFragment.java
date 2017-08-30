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
 * Created by hh on 2017/1/13.
 */
public class WallPaperCalculationFragment extends BaseFragment implements View.OnClickListener{

    private EditText wallHeightEdittext;
    private EditText wallLongEdittext;
    private EditText wallCountEdittext;
    private EditText wallPaperSizeEdit;
    private EditText wallPaperPriceEdit;


    @Override
    public void setTitleText() {
        titleText.setText("壁纸计算");
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallpaper_calculation,container,false);
    }

    public void initUi(View view) {

        resultLayout = (RelativeLayout)view.findViewById(R.id.result_layout);
        scrollView = (ScrollView)view. findViewById(R.id.scrollView);
        wallHeightEdittext = (EditText) view.findViewById(R.id.wall_height_edittext);
        wallLongEdittext = (EditText) view.findViewById(R.id.wall_long_edittext);
        wallCountEdittext = (EditText) view.findViewById(R.id.wall_count_edittext);
        wallPaperSizeEdit = (EditText) view.findViewById(R.id.wall_paper_size_edit);
        wallPaperPriceEdit = (EditText) view.findViewById(R.id.wall_paper_price_edit);
        totalCountResult = (TextView) view.findViewById(R.id.wall_paper_count_text);
        totalPriceResult = (TextView) view.findViewById(R.id.wall_paper_total_price_text);
        addListener(view);
    }

    private void addListener(View view) {
        save.setOnClickListener(this);
        view.findViewById(R.id.restart).setOnClickListener(this);
        view.findViewById(R.id.calculate).setOnClickListener(this);
        wallHeightEdittext.addTextChangedListener(mTextWatcher);
        wallLongEdittext.addTextChangedListener(mTextWatcher);
        wallCountEdittext.addTextChangedListener(mTextWatcher);
        wallPaperSizeEdit.addTextChangedListener(mTextWatcher);
        wallPaperPriceEdit.addTextChangedListener(mTextWatcher);
    }

    public void fillData() {

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
            if (info.getHouseLong() != null)
                wallLongEdittext .setText(info.getHouseLong());
            if (info.getHouseHeight() != null)
                wallHeightEdittext.setText(info.getHouseHeight());
            if (info.getmSize() != null)
                wallPaperSizeEdit.setText(info.getmSize());
            if (info.getmPerPrice() != null)
                wallPaperPriceEdit.setText(info.getmPerPrice());
            if(info.getDoorCount() != null)
                wallCountEdittext.setText(info.getDoorCount());

        }

    }

    public void clearData(){
        super.clearData();
        wallLongEdittext.setText("");
        wallHeightEdittext.setText("");
        wallCountEdittext.setText("");
        wallPaperPriceEdit.setText("");
    }

    public void calculateData(){

        int countResult,priceResult;
        /**计算墙面面积*/
        float wallLongData,wallHeightData,wallArea;
        int wallCountData = 0;
        if(TextUtils.isEmpty(wallLongEdittext.getText()) || TextUtils.isEmpty(wallHeightEdittext.getText()) || TextUtils.isEmpty(wallCountEdittext.getText())){
            Toast.makeText(getActivity(),"墙面信息不能为空哦",Toast.LENGTH_SHORT).show();
            return;
        }
        wallLongData = Float.parseFloat(wallLongEdittext.getText().toString());
        wallHeightData = Float.parseFloat(wallHeightEdittext.getText().toString());
        wallCountData = Integer.parseInt(wallCountEdittext.getText().toString());



        if(wallCountData>=1){
            wallArea = wallLongData*wallHeightData*wallCountData;
        }else{
            Toast.makeText(getActivity(),"请输入正确的墙面信息", Toast.LENGTH_SHORT).show();
            return;
        }
        float wallPaperSize = 0.0f;
        if(!TextUtils.isEmpty(wallPaperSizeEdit.getText())){
            wallPaperSize = Float.parseFloat(wallPaperSizeEdit.getText().toString());
        }else {
            wallPaperSize = Float.parseFloat(wallPaperSizeEdit.getHint().toString());
        }
        if(wallPaperSize < 0){
            Toast.makeText(getActivity(),"请输入正确的壁纸规格", Toast.LENGTH_SHORT).show();
            return;
        }
        //计算每卷壁纸可以裁多少幅
        double paperFuShu = 10/wallHeightData;

//        int paperFuShu = (int) (10/(wallHeightData+0.25));
        //计算需要多少幅墙纸
        int paperFushuCount = (int)Math.ceil(wallLongData / (wallPaperSize*0.1));
        //计算所需墙纸卷数
        if (paperFuShu > 1) {
            //国内一般房间高度都是小于10米的，此处为了防止特殊情况，直接取整导致paperFuShu为0.所以当输入房间高度大于10时，不取整。
            countResult = (int) Math.ceil(paperFushuCount / (int)paperFuShu*1.05);
        }else{
            countResult = (int) Math.ceil(paperFushuCount / paperFuShu*1.05);
        }
            resultLayout.setVisibility(View.VISIBLE);
//        if(wallPaperSize > 0){
//            countResult = (int)Math.ceil( ((wallArea)/(wallPaperSize*0.9)));
            totalCountResult.setText(String.valueOf(countResult));
//        }else{
//            Toast.makeText(getActivity(),"请输入正确的壁纸规格", Toast.LENGTH_SHORT).show();
//            return;
//        }
        float wallPaperPrice = 0.0f;
        if(!TextUtils.isEmpty(wallPaperPriceEdit.getText())){
            wallPaperPrice = Float.parseFloat(wallPaperPriceEdit.getText().toString());
        }
        if(wallPaperPrice > 0){
            priceResult  =(int)Math.ceil( wallPaperPrice*countResult);
            totalPriceResult.setText(String.valueOf(priceResult));
        }else{
            Toast.makeText(getActivity(),"请输入正确的壁纸单价", Toast.LENGTH_SHORT).show();
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
        historyInfo.setmName("壁纸计算");
        historyInfo.setmResultCount(totalCountResult.getText().toString().trim());
        historyInfo.setmResultPrice(totalPriceResult.getText().toString().trim());
        historyInfo.setmTime(dateFormat.format(date));
        historyInfo.setmType(Constant.CaculatorType.WALLPAPER.name());
        historyInfo.setmPerPrice(wallPaperPriceEdit.getText().toString().trim());
        historyInfo.setmSize(wallPaperSizeEdit.getText().toString().trim());
        historyInfo.setHouseLong(wallLongEdittext.getText().toString().trim());
        historyInfo.setHouseHeight(wallHeightEdittext.getText().toString().trim());
        historyInfo.setDoorCount(wallCountEdittext.getText().toString().trim());

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
