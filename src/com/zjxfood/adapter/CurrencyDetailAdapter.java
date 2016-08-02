package com.zjxfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjxfood.activity.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/7.
 */
public class CurrencyDetailAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<HashMap<String,Object>> mLists;
    public CurrencyDetailAdapter(Context context, ArrayList<HashMap<String,Object>> lists){
        this.mContext = context;
        this.mLists = lists;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public void notify(ArrayList<HashMap<String, Object>> list){
        this.mLists.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder = null;
        if(view==null){
            mHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.currency_detail_item,null);
            mHolder.mDate = (TextView) view.findViewById(R.id.currcy_detail_item_name);
            mHolder.mValue = (TextView) view.findViewById(R.id.currcy_detail_item_value);
            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }
        if(mLists.get(i).get("CreateTime")!=null){
            mHolder.mDate.setText(mLists.get(i).get("CreateTime").toString());
        }
        if(mLists.get(i).get("Amount")!=null){
            mHolder.mValue.setText(mLists.get(i).get("Amount").toString());
        }
        return view;
    }
    class ViewHolder{
        private TextView mDate;
        private TextView mValue;
    }
}
