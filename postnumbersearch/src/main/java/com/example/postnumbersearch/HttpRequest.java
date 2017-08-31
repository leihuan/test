package com.example.postnumbersearch;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lh on 2017/8/2.
 */
public class HttpRequest {
    private String url_head = "http://api.avatardata.cn/PostNumber/";
    private String appKey = "a1978449194b49cab3a1378f568b1cc4";
    private String postnumber = "";
    private String page = "1";
    private String rows = "50";
    private URL url = null;
    private HttpURLConnection connection = null;
    private InputStreamReader in = null;
    private OnHttpRequestListener onHttpRequestListener;
    private Context mContext;
    private MySharePreference mySharePreference;

     public  HttpRequest(Context mContext,OnHttpRequestListener onHttpRequestListener){
         this.mContext = mContext;
         this.onHttpRequestListener = onHttpRequestListener;
         mySharePreference = new MySharePreference(mContext);
     }


    private String buildUrl(){
        String url="";
        if (isPostNumber(postnumber)) {
            url = url_head +"QueryPostnumber?key="+ appKey + "&postnumber=" + postnumber + "&page=" + page + "&rows=" + rows;

        }else {
            String param = postnumber;
                try {
                    param = URLEncoder.encode(param, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            url = url_head +"QueryAddress?key="+appKey + "&address=" + param + "&page=" + page + "&rows=" + rows;
         }
        return url;
    }

//    public void setPostnumber(String postnumber) {
//        this.postnumber = postnumber;
//    }

    private boolean isPostNumber(String value){
        boolean isNum = value.matches("[0-9]+");
        return  isNum;
    }
    StringBuffer strBuffer;
    public void requestNetData(String param) {

        if(!Util.isNetworkConnected(mContext)){
            Toast.makeText(mContext,"网络连接不可用",Toast.LENGTH_SHORT).show();
        }
        if(param.isEmpty()){
            Toast.makeText(mContext,"查询信息不能为空",Toast.LENGTH_SHORT).show();
        }
        this.postnumber = param;
        if (!TextUtils.isEmpty(mySharePreference.getValue(postnumber))){
            strBuffer = new StringBuffer();
            strBuffer.append(mySharePreference.getValue(postnumber));
            myHandler.sendEmptyMessage(0);
            return;
        }
        onHttpRequestListener.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    url = new URL(buildUrl());
                    if(url == null) return;
                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setConnectTimeout(5 * 1000);
//                    connection.setReadTimeout(5 * 1000);
                    connection.setUseCaches(true);
                    in = new InputStreamReader(connection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(in);
                    strBuffer = new StringBuffer();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        strBuffer.append(line);
                    }
                   myHandler.sendEmptyMessage(0);

                }catch (Exception e){
                    Message msg = myHandler.obtainMessage();
                    msg.what=1;
                    msg.obj=e;
                    myHandler.sendMessage(msg);

                }finally {
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //网络请求成功
                case 0:
                    DataBean dataBean = parserData(strBuffer.toString());
                    if(dataBean.getError_code() == 0){
                        onHttpRequestListener.onSuccess(dataBean);
                    }else {
                        onHttpRequestListener.onError(dataBean.getReason());
                    }
                    break;
                //网络请求失败
                case 1:
                    onHttpRequestListener.onError(msg.obj.toString());
            }
        }
    };

    public DataBean parserData(String str){
        DataBean data = new DataBean();
        try {
            JSONObject jsonObject = new JSONObject(str);
            data.setError_code(jsonObject.optInt("error_code"));
            data.setReason(jsonObject.getString("reason"));
            if(data.getError_code() != 0){
                return data;
            }
            mySharePreference.putValue(postnumber,str);
            data.setTotal(jsonObject.optInt("total"));
            data.setError_code(jsonObject.optInt("error_code"));
            data.setReason(jsonObject.getString("reason"));
            data.setSingerResult(jsonObject.optString("SingerResult"));
            JSONArray jsonArray = jsonObject.optJSONArray("result");
            List<AddressInfo> result = new ArrayList<AddressInfo>();
            for(int i = 0; i<jsonArray.length();i++){
                JSONObject jo1 = jsonArray.optJSONObject(i);
                AddressInfo addressInfo = new AddressInfo();
                addressInfo.setAddress(jo1.optString("address"));
                addressInfo.setCity(jo1.optString("city"));
                addressInfo.setDistrict(jo1.optString("district"));
                addressInfo.setPostnumber(jo1.optString("postnumber"));
                addressInfo.setJd(jo1.optString("jd"));
                addressInfo.setProvince(jo1.optString("province"));
                result.add(addressInfo);
            }
            data.setResult(result);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return data;
    }
}
