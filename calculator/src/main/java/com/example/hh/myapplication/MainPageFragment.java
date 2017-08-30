package com.example.hh.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.hh.myapplication.util.Constant;

/**
 * Created by hh on 2017/1/13.
 */
public class MainPageFragment extends Fragment implements View.OnClickListener{

    private LinearLayout coatingCalculationLayout;
    private LinearLayout floorCalculationLayout;
    private LinearLayout wallCalculationLayout;
    private LinearLayout wallpaperCalculationLayout;
    private LinearLayout floorTileCalculationLayout;
    private LinearLayout curtainCalculatorLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main,container,false);
        initUI(view);
        return view;

    }

    public void initUI(View view){
        coatingCalculationLayout = (LinearLayout)view.findViewById(R.id.coating_calculation_layout);
        floorCalculationLayout = (LinearLayout)view.findViewById(R.id.floor_calculation_layout);
        wallCalculationLayout = (LinearLayout)view.findViewById(R.id.wall_calculation_layout);
        wallpaperCalculationLayout = (LinearLayout)view.findViewById(R.id.wallpaper_calculation_layout);
        floorTileCalculationLayout = (LinearLayout)view.findViewById(R.id.floor_tile_calculation_layout);
        curtainCalculatorLayout = (LinearLayout)view.findViewById(R.id.curtain_calculator_layout);

        coatingCalculationLayout.setOnClickListener(this);
        floorCalculationLayout.setOnClickListener(this);
        wallCalculationLayout.setOnClickListener(this);
        wallpaperCalculationLayout.setOnClickListener(this);
        floorTileCalculationLayout.setOnClickListener(this);
        curtainCalculatorLayout.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Constant.CaculatorType  caculatorType = Constant.CaculatorType.COATING;
        switch (v.getId()){
            case  R.id.coating_calculation_layout:
                caculatorType = Constant.CaculatorType.COATING;
                break;
            case  R.id.floor_calculation_layout:
                caculatorType = Constant.CaculatorType.FLOOR;
                break;
            case  R.id.wall_calculation_layout:
                caculatorType = Constant.CaculatorType.WALL;
                break;
            case  R.id.wallpaper_calculation_layout:
                caculatorType = Constant.CaculatorType.WALLPAPER;
                break;
            case  R.id.floor_tile_calculation_layout:
                caculatorType = Constant.CaculatorType.FLOORTILE;
                break;
            case  R.id.curtain_calculator_layout:
                caculatorType = Constant.CaculatorType.CURTAIN;
                break;
        }

        Intent intent = new Intent();
        intent.setClass(getActivity(), CaculatorActivity.class);
        intent.putExtra("type",caculatorType.name());
        startActivity(intent);
    }


}
