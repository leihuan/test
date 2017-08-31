package com.example.postnumbersearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hh on 2017/2/13.
 * 历史记录RecyclerView的适配器
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private  Context mContext;
    private List<AddressInfo> addressInfoList;
    private View.OnClickListener mOnClickListener;
    public AddressAdapter(Context mContext, List<AddressInfo> infoList){
        this.mContext = mContext;
        this.addressInfoList = infoList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_address_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.postNumber.setText("邮政编码："+addressInfoList.get(position).getPostnumber());
        holder.province.setText("省份："+addressInfoList.get(position).getProvince());
        holder.city.setText("城市："+addressInfoList.get(position).getCity());
        holder.district.setText("区（县）："+addressInfoList.get(position).getDistrict());
        holder.avenue.setText("地址："+addressInfoList.get(position).getProvince()+"  "+addressInfoList.get(position).getCity()+
                "  "+addressInfoList.get(position).getDistrict()+"  "+addressInfoList.get(position).getAddress());
    }


    @Override
    public int getItemCount() {
        return addressInfoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView postNumber;
        private TextView province;
        private TextView city;
        private TextView district;
        private TextView avenue;




        public ViewHolder(View view) {
            super(view);
            postNumber = (TextView) view.findViewById(R.id.post_number);
            province = (TextView) view.findViewById(R.id.province);
            city = (TextView) view.findViewById(R.id.city);
            district = (TextView) view.findViewById(R.id.district);
            avenue = (TextView) view.findViewById(R.id.avenue);

        }
    }

}
