package com.example.hh.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by lh on 2017/1/15.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> list;

        public MainViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int arg0) {

            return list.get(arg0);
        }

        @Override
        public int getCount() {
            return list.size();
        }

}
