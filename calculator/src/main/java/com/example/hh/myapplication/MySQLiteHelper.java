package com.example.hh.myapplication;

/**
 * Created by lh on 2017/2/19.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.widget.Toast;

import com.example.hh.myapplication.util.Constant;
import com.example.hh.myapplication.util.HistoryInfo;
import com.example.hh.myapplication.util.HouseInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class MySQLiteHelper extends SQLiteOpenHelper{
    //调用父类构造器
    public MySQLiteHelper(Context context, String name, CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }


    //创建MySQLiteOpenHelper辅助类对象
    static MySQLiteHelper getDbHelper(Context context) {
        return   new MySQLiteHelper(context, "my.db", null, 1);

    }
    /**
     * 当数据库首次创建时执行该方法，一般将创建表等初始化操作放在该方法中执行.
     * 重写onCreate方法，调用execSQL方法创建表
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists history("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "mName varchar,"
                + "mResultCount varchar,"
                + "mResultPrice varchar,"
                + "mTime varchar,"
                + "mType varchar,"
                + "houseLong varchar,"
                + "houseWidth varchar,"
                + "houseHeight varchar,"
                + "doorHeight varchar,"
                + "doorWidth varchar,"
                + "doorCount varchar,"
                + "windowHeight varchar,"
                + "windowWidth varchar,"
                + "windowCount varchar,"
                + "mPerPrice varchar,"
                + "mPerMaterialLong varchar,"
                + "mPerMaterialWidth varchar,"
                + "mSize varchar"
                +")");
        db.execSQL("create table if not exists houseInfo("
                + "_id INTEGER PRIMARY KEY,"
                + "houseLong varchar,"
                + "houseWidth varchar,"
                + "houseHeight varchar,"
                + "doorHeight varchar,"
                + "doorWidth varchar,"
                + "doorCount varchar,"
                + "windowHeight varchar,"
                + "windowWidth varchar,"
                + "windowCount varchar"
                +")");

    }

    //当打开数据库时传入的版本号与当前的版本号不同时会调用该方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    //向数据库中插入和更新数据
    public void insertAndUpdateData(Context context,Object historyInfo,String tableName){
        //获取数据库对象
        SQLiteDatabase db = this.getWritableDatabase();
        //使用execSQL方法向表中插入数据
//        db.execSQL("insert into hero_info(name,level) values('bb',0)");
        //使用insert方法向表中插入数据
        // 取得实现的接口或者父类的属性
        Class<?> clazz = historyInfo.getClass();
        Field[] filed1 = clazz.getFields();
        ContentValues values = new ContentValues();
        for (int j = 0; j < filed1.length; j++) {
            Field f = filed1[j];
            // 权限修饰符
            int mo = f.getModifiers();
            String priv = Modifier.toString(mo);
            // 属性类型
            Class<?> type = f.getType();
            System.out.println(priv + " " + type.getName() + " " + f.getName() + ";");

            if (type.getName().equals("java.lang.String")) {
                f.setAccessible(true); //设置些属性是可以访问的
                String val;
                try {
                    val = (String) f.get(historyInfo);//得到此属性的值
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    val = "";
                }
                if (val!=null && !val.equals("")) {
                    values.put(f.getName(), val);
                }
            }

        }

        //调用方法插入数据
        db.insert(tableName, "", values);
        Log.v("LH","存入数据："+values);
        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
        //关闭SQLiteDatabase对象
        db.close();

    }

    //从数据库中查询房屋信息
    public HouseInfo getHouseInfo(){
        HouseInfo houseInfo = new HouseInfo();
        //获得数据库对象
        SQLiteDatabase db = this.getReadableDatabase();
        //查询表中的数据
        Cursor cursor = db.query(Constant.housInfoTableName, null, null, null, null, null, "_id desc");
        //获取name列的索引
        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
            houseInfo.setWindowCount(cursor.getString(cursor.getColumnIndex("windowCount")));
            houseInfo.setWindowWidth(cursor.getString(cursor.getColumnIndex("windowWidth")));
            houseInfo.setWindowHeight(cursor.getString(cursor.getColumnIndex("windowHeight")));
            houseInfo.setDoorCount(cursor.getString(cursor.getColumnIndex("doorCount")));
            houseInfo.setDoorHeight(cursor.getString(cursor.getColumnIndex("doorHeight")));
            houseInfo.setDoorWidth(cursor.getString(cursor.getColumnIndex("doorWidth")));
            houseInfo.setHouseHeight(cursor.getString(cursor.getColumnIndex("houseHeight")));
            houseInfo.setHouseWidth(cursor.getString(cursor.getColumnIndex("houseWidth")));
            houseInfo.setHouseLong(cursor.getString(cursor.getColumnIndex("houseLong")));
        }
        cursor.close();//关闭结果集
        db.close();//关闭数据库对象
        return houseInfo;
    }



    //从数据库中查询历史记录数据
    public ArrayList<HistoryInfo> getHistoryList(){
        ArrayList<HistoryInfo> list = new ArrayList<>();
        String result = "";
        //获得数据库对象
        SQLiteDatabase db = this.getReadableDatabase();
        //查询表中的数据
        Cursor cursor = db.query(Constant.historyTableName, null, null, null, null, null, "_id desc");
        //获取name列的索引
        int mNameIndex = cursor.getColumnIndex("mName");
        int mTimeIndex = cursor.getColumnIndex("mTime");
        int mTypeIndex = cursor.getColumnIndex("mType");
        int mResultCountIndex = cursor.getColumnIndex("mResultCount");
        int mResultPriceIndex = cursor.getColumnIndex("mResultPrice");
        int houseLongIndex = cursor.getColumnIndex("houseLong");
        int houseWidtheIndex = cursor.getColumnIndex("houseWidth");
        int houseHeightIndex = cursor.getColumnIndex("houseHeight");
        int doorHeightIndex = cursor.getColumnIndex("doorHeight");
        int doorWidthIndex = cursor.getColumnIndex("doorWidth");
        int doorCountIndex = cursor.getColumnIndex("doorCount");
        int windowHeightIndex = cursor.getColumnIndex("windowHeight");
        int windowWidthIndex = cursor.getColumnIndex("windowWidth");
        int windowCountIndex = cursor.getColumnIndex("windowCount");
        int mPerPriceIndex = cursor.getColumnIndex("mPerPrice");
        int mPerMaterialLongIndex = cursor.getColumnIndex("mPerMaterialLong");
        int mPerMaterialWidthIndex = cursor.getColumnIndex("mPerMaterialWidth");
        int mSize = cursor.getColumnIndex("mSize");

        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
            result = result + cursor.getString(mNameIndex)+ "\t\t";
            result = result + cursor.getString(mTimeIndex)+"       \n";

            HistoryInfo historyInfo = new HistoryInfo();
            historyInfo.setmTime(cursor.getString(mTimeIndex));
            historyInfo.setmName(cursor.getString(mNameIndex));
            historyInfo.setmResultCount(cursor.getString(mResultCountIndex));
            historyInfo.setmResultPrice(cursor.getString(mResultPriceIndex));
            historyInfo.setmType(cursor.getString(mTypeIndex));
            historyInfo.setHouseHeight(cursor.getString(houseHeightIndex));
            historyInfo.setHouseLong(cursor.getString(houseLongIndex));
            historyInfo.setHouseWidth(cursor.getString(houseWidtheIndex));
            historyInfo.setWindowWidth(cursor.getString(windowWidthIndex));
            historyInfo.setWindowHeight(cursor.getString(windowHeightIndex));
            historyInfo.setWindowCount(cursor.getString(windowCountIndex));
            historyInfo.setDoorCount(cursor.getString(doorCountIndex));
            historyInfo.setDoorHeight(cursor.getString(doorHeightIndex));
            historyInfo.setDoorWidth(cursor.getString(doorWidthIndex));
            historyInfo.setmPerPrice(cursor.getString(mPerPriceIndex));
            historyInfo.setmPerMaterialWidth(cursor.getString(mPerMaterialWidthIndex));
            historyInfo.setmPerMaterialLong(cursor.getString(mPerMaterialLongIndex));
            historyInfo.setmSize(cursor.getString(mSize));
            list.add(historyInfo);
        }
        System.out.println(result);

        cursor.close();//关闭结果集
        db.close();//关闭数据库对象
        return list;
    }

    /**清空某张表的数据*/
    public void deleteAll(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, "", null);
    }
}
