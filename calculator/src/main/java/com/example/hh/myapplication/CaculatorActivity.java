package com.example.hh.myapplication;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.hh.myapplication.util.Constant;
import com.example.hh.myapplication.util.HistoryInfo;
import com.example.hh.myapplication.util.HouseInfo;

/**
 * Created by hh on 2017/1/13.
 */
public class CaculatorActivity extends Activity {

    private Constant.CaculatorType  type;
    private String from;
    private HistoryInfo info;
    private CoatingCalculationFragment coatingCalculationFragment;
    private FloorCalculationFragment floorCalculationFragment;
    private WallTileCalculationFragment wallTileCalculationFragment;
    private WallPaperCalculationFragment wallPaperCalculationFragment;
    private FloorTileCalculationFragment floorTileCalculationFragment;
    private CurtainCalculationFragment curtainCalculationFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        initData();
    }

    private void initData() {

        type = Constant.CaculatorType.valueOf(getIntent().getStringExtra("type"));
        coatingCalculationFragment = new CoatingCalculationFragment();
        floorCalculationFragment = new FloorCalculationFragment();
        wallTileCalculationFragment = new WallTileCalculationFragment();
        wallPaperCalculationFragment = new WallPaperCalculationFragment();
        floorTileCalculationFragment = new FloorTileCalculationFragment();
        curtainCalculationFragment = new CurtainCalculationFragment();
        loadFrament(type);
    }


    private void loadFrament(Constant.CaculatorType type) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (type){
            case COATING:
                transaction.add(R.id.container,coatingCalculationFragment);
                break;
            case FLOOR:
                transaction.add(R.id.container,floorCalculationFragment);
                break;
            case WALL:
                transaction.add(R.id.container, wallTileCalculationFragment);
                break;
            case WALLPAPER:
                transaction.add(R.id.container,wallPaperCalculationFragment);
                break;
            case FLOORTILE:
                transaction.add(R.id.container,floorTileCalculationFragment);
                break;
            case CURTAIN:
                transaction.add(R.id.container,curtainCalculationFragment);
                break;

        }
        transaction.commitAllowingStateLoss();
    }
}

