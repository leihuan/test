package com.example.hh.myapplication.util;

/**
 * Created by hh on 2017/1/13.
 */
public class Constant {
    public static  enum CaculatorType{
        COATING,
        FLOOR,
        WALL,
        WALLPAPER,
        FLOORTILE,
        CURTAIN
    }
    public static  enum OperationType{
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE
    }
    public static final String DBName = "user_data";
    public static final String historyKey = "history";
    public static final  String historyTableName = "history";
    public static final  String housInfoTableName = "houseInfo";
    public static final String SHOW_HISTORY  = "showHistory";
}
