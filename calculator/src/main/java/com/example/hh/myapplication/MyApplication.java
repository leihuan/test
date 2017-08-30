package com.example.hh.myapplication;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        new MySQLiteHelper(context, "my.db", null, 1);
    }

    public static Context getContextObject(){
        return context;
    }  
}  