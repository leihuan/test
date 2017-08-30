package com.example.hh.myapplication.util;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by lh on 2017/3/7.
 */
public class MyTextWatcher implements TextWatcher{
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String temp = s.toString();
        int posDot = temp.indexOf(".");
        if (posDot <= 0) return;
        if (temp.length() - posDot - 1 > 2)
        {
            s.delete(posDot + 3, s.length());
        }
    }
}
