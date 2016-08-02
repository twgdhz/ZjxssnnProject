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
public class CurrcyAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<HashMap<String,Object>> mLists;
    public CurrcyAdapter(Context context,ArrayList<HashMap<String,Object>> lists){
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder = null;
        if(view==null){
            mHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.currency_list_item,null);
            mHolder.mName = (TextView) view.findViewById(R.id.currcy_item_name);
            mHolder.mValue = (TextView) view.findViewById(R.id.currcy_item_value);
            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }
        if(mLists.get(i).get("DisplayName")!=null){
            mHolder.mName.setText(mLists.get(i).get("DisplayName").toString());
        }
        if(mLists.get(i).get("Amount")!=null && mLists.get(i).get("Operator")!=null){
            if(mLists.get(i).get("Operator").toString().equals("+")){
                mHolder.mValue.setText(mLists.get(i).get("Amount").toString());
            }else if(mLists.get(i).get("Operator").toString().equals("-")){
                mHolder.mValue.setText(mLists.get(i).get("Amount").toString());
            }

        }
        return view;
    }
    class ViewHolder{
        private TextView mName;
        private TextView mValue;
    }
}
