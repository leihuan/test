package com.example.hh.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hh.myapplication.util.Constant;
import com.example.hh.myapplication.util.HouseInfo;
import com.example.hh.myapplication.util.MyTextWatcher;

/**
 * Created by hh on 2017/1/13.
 */
public class InfoEditFragment extends Fragment implements View.OnClickListener{

    private EditText houseLong,houseWidth,houseHeight,doorHeight,doorWidth,doorCount,windowHeight,windowWidth,windowCount;
    private Button saveBtn;
    MyTextWatcher mTextWatcher = new MyTextWatcher();
    private MySQLiteHelper myHelper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_info_edit,container,false);
        initUI(view);
        fillData();
        //创建MySQLiteOpenHelper辅助类对象
        myHelper = new MySQLiteHelper(this.getActivity(), "my.db", null, 1);
        return view;
    }

    public void fillData() {

        myHelper = new MySQLiteHelper(this.getActivity(), "my.db", null, 1);
        HouseInfo houseInfo = myHelper.getHouseInfo();
        if (houseInfo != null){
            if (houseInfo.getHouseLong() != null)
                houseLong.setText(houseInfo.getHouseLong());
            if (houseInfo.getHouseWidth() != null)
                houseWidth.setText(houseInfo.getHouseWidth());
            if (houseInfo.getHouseHeight() != null)
                houseHeight.setText(houseInfo.getHouseHeight());
            if (houseInfo.getDoorHeight() != null)
                doorHeight.setText(houseInfo.getDoorHeight());
            if (houseInfo.getDoorWidth() != null)
                doorWidth.setText(houseInfo.getDoorWidth());
            if (houseInfo.getDoorCount() != null)
                doorCount.setText(houseInfo.getDoorCount());
            if (houseInfo.getWindowHeight() != null)
                windowHeight.setText(houseInfo.getWindowHeight());
            if (houseInfo.getWindowWidth() != null)
                windowWidth.setText(houseInfo.getWindowWidth());
            if (houseInfo.getWindowCount() != null)
                windowCount.setText(houseInfo.getWindowCount());
        }
    }

    private void initUI(View view) {
        houseLong = (EditText)view.findViewById(R.id.house_long_edittext);
        houseWidth = (EditText)view.findViewById(R.id.house_width_edittext);
        houseHeight = (EditText)view.findViewById(R.id.house_height_edittext);
        doorHeight = (EditText)view.findViewById(R.id.door_height_edit);
        doorWidth = (EditText)view.findViewById(R.id.door_width_edit);
        doorCount = (EditText)view.findViewById(R.id.door_count_edit);
        windowHeight = (EditText)view.findViewById(R.id.window_height_edit);
        windowWidth = (EditText)view.findViewById(R.id.window_width_edit);
        windowCount = (EditText)view.findViewById(R.id.window_count_edit);
        saveBtn = (Button)view.findViewById(R.id.save);
        saveBtn.setOnClickListener(this);

        houseLong.addTextChangedListener(mTextWatcher);
        houseWidth.addTextChangedListener(mTextWatcher);
        houseHeight.addTextChangedListener(mTextWatcher);
        doorHeight.addTextChangedListener(mTextWatcher);
        doorWidth.addTextChangedListener(mTextWatcher);
        doorCount.addTextChangedListener(mTextWatcher);
        windowHeight.addTextChangedListener(mTextWatcher);
        windowWidth.addTextChangedListener(mTextWatcher);
        windowCount.addTextChangedListener(mTextWatcher);
    }

    @Override
    public  void onClick(View v){
        switch (v.getId()){
            case R.id.save:
                saveData();
                Toast.makeText(getActivity(),"保存成功",Toast.LENGTH_SHORT).show();
                break;
        }

    }

    public  void saveData(){
        HouseInfo houseInfo = new HouseInfo();
        houseInfo.setHouseLong(houseLong.getText().toString());
        houseInfo.setHouseWidth(houseWidth.getText().toString());
        houseInfo.setHouseHeight(houseHeight.getText().toString());
        houseInfo.setDoorHeight(doorHeight.getText().toString());
        houseInfo.setDoorWidth(doorWidth.getText().toString());
        houseInfo.setDoorCount(doorCount.getText().toString());
        houseInfo.setWindowHeight(windowHeight.getText().toString());
        houseInfo.setWindowWidth(windowWidth.getText().toString());
        houseInfo.setWindowCount(windowCount.getText().toString());
        myHelper.deleteAll(Constant.housInfoTableName);
        myHelper.insertAndUpdateData(this.getActivity(),houseInfo, Constant.housInfoTableName);


    };
}
