package com.example.hh.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.hh.myapplication.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private MainPageFragment mainPageFragment;
    private InfoEditFragment infoEditFragment;
    private HistoryFragment historyFragment;
    private RadioGroup radioGroup;
    private RadioButton mainBtn, infoEditBtn, historyBtn;
    private ArrayList<Fragment> fragmentList;
    private ViewPager viewPager;
    private TextView title;
    private ImageView delete;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initViewPager(savedInstanceState);
    }

    private void initUI() {
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        mainBtn = (RadioButton)findViewById(R.id.main_button);
        infoEditBtn = (RadioButton)findViewById(R.id.info_edit_button);
        historyBtn = (RadioButton)findViewById(R.id.history_button);
        viewPager = (ViewPager)findViewById(R.id.main_viewpager);
        title = (TextView)findViewById(R.id.title);
        mainBtn.setChecked(true);
        delete = (ImageView) findViewById(R.id.delete);
        delete.setOnClickListener(this);
    }

    /**
     * 初始化fragment
     */
    public void initViewPager(Bundle savedInstanceState) {

        viewPager.setOffscreenPageLimit(2);
        fragmentList = new ArrayList<Fragment>();
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> flist = fm.getFragments();
        if (savedInstanceState != null && flist != null) {
                for (int i = 0; i < flist.size(); i++) {
                    if (i == 0 && flist.get(0) instanceof MainPageFragment) {
                        mainPageFragment = (MainPageFragment) flist.get(0);
                    } else if (i == 1
                            && flist.get(1) instanceof InfoEditFragment) {
                        infoEditFragment = (InfoEditFragment) flist.get(1);
                    } else if (i == 2
                            && flist.get(2) instanceof HistoryFragment) {
                        historyFragment = (HistoryFragment) flist.get(2);
                    }
                }
        }else {
            mainPageFragment = new MainPageFragment();
            infoEditFragment = new InfoEditFragment();
            historyFragment = new HistoryFragment();
        }
        fragmentList.add(mainPageFragment);
        fragmentList.add(infoEditFragment);
        fragmentList.add(historyFragment);

        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(),
                fragmentList);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(0); // 设置初始页面为第一页
        delete.setVisibility(View.INVISIBLE);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.main_button);
                        break;
                    case 1:
                        radioGroup.check(R.id.info_edit_button);
                        break;
                    case 2:
                        radioGroup.check(R.id.history_button);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.main_button:
                        viewPager.setCurrentItem(0);
                        title.setText(getResources().getString(R.string.main_title));
                        delete.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.info_edit_button:
                        viewPager.setCurrentItem(1);
                        delete.setVisibility(View.INVISIBLE);
                        title.setText(getResources().getString(R.string.info));
                        break;
                    case R.id.history_button:
                        viewPager.setCurrentItem(2);
                        delete.setVisibility(View.VISIBLE);
                        title.setText(getResources().getString(R.string.history));
                }
            }
        });

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete:
                new MySQLiteHelper(this, "my.db", null, 1).deleteAll(Constant.historyTableName);
                Intent intent = new  Intent();
                intent.setAction(Constant.SHOW_HISTORY);
                this.sendBroadcast(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
