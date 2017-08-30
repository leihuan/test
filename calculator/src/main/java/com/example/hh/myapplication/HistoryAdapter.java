package com.example.hh.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hh.myapplication.util.Constant;
import com.example.hh.myapplication.util.HistoryInfo;
import com.example.hh.myapplication.util.HouseInfo;

import java.util.List;

/**
 * Created by hh on 2017/2/13.
 * 历史记录RecyclerView的适配器
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private  Context mContext;
    private List<HistoryInfo> historyList;
    private View.OnClickListener mOnClickListener;
    private int curPosition;
    public HistoryAdapter(Context mContext, List<HistoryInfo> infoList){
        this.mContext = mContext;
        this.historyList = infoList;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caculatePageSwith(curPosition);
            }
        };
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_hostory_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        curPosition = position;
        holder.mName.setText(historyList.get(position).getmName());
        holder.mTime.setText(historyList.get(position).getmTime());
        if(!TextUtils.isEmpty(historyList.get(position).getmResultCount())) {
            holder.mResultCount.setText("数量：" + historyList.get(position).getmResultCount());
            holder.mResultPrice.setText("总价：" + historyList.get(position).getmResultPrice());
            holder.mResultCount.setVisibility(View.VISIBLE);
            holder.mResultPrice.setVisibility(View.VISIBLE);
        }else{
            holder.mResultCount.setVisibility(View.INVISIBLE);
            holder.mResultPrice.setVisibility(View.INVISIBLE);
        }
        holder.mArrow.setOnClickListener(mOnClickListener);
        holder.rootLayout.setOnClickListener(mOnClickListener);
    }


    @Override
    public int getItemCount() {
        return historyList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private TextView mTime;
        private TextView mResultCount;
        private TextView mResultPrice;
        private ImageView mArrow;
        private LinearLayout rootLayout;

        public ViewHolder(View view) {
            super(view);
            this.mName = (TextView) view.findViewById(R.id.name);
            this.mTime = (TextView) view.findViewById(R.id.time);
            this.mResultCount = (TextView) view.findViewById(R.id.result_count);
            this.mResultPrice = (TextView) view.findViewById(R.id.result_price);
            this.mArrow = (ImageView)view.findViewById(R.id.next_arrow);
            this.rootLayout = (LinearLayout)view.findViewById(R.id.root_layout);

        }
    }


    public void caculatePageSwith(int position){
        Intent intent = new Intent();
        intent.putExtra("position",position);
        intent.putExtra("from",true);
        intent.putExtra("type",historyList.get(position).getmType());
        intent.setClass(mContext, CaculatorActivity.class);
        mContext.startActivity(intent);
    }
}
