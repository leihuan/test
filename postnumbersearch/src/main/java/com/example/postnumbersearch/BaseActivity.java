package com.example.postnumbersearch;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by lh on 2017/8/22.
 */
public abstract class BaseActivity extends Activity {


    public String getResourceText(int id) {
        return getResources().getString(id);
    }


}
