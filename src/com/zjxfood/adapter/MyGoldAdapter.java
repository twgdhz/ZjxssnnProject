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
 * Created by Administrator on 2016/6/3.
 */
public class MyGoldAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<HashMap<String, Object>> mList;
    public MyGoldAdapter(Context context,ArrayList<HashMap<String, Object>> list){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mList = list;
    }
    @Override
    public int getCount() {
        return mList.size();
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
        this.mList.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder = null;
        if(view==null){
            mHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.my_gold_list_item,null);
            mHolder.mTitleText = (TextView) view.findViewById(R.id.my_bill_item_text_title);
            mHolder.mDateText = (TextView) view.findViewById(R.id.my_bill_item_text_date);
            mHolder.mNumText = (TextView) view.findViewById(R.id.my_bill_item_text_money);
            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }
        mHolder.mTitleText.setText(mList.get(i).get("ChangeTypeName").toString());
        mHolder.mDateText.setText(mList.get(i).get("CreateTime").toString());
        mHolder.mNumText.setText(mList.get(i).get("Amount").toString());
        return view;
    }

    class ViewHolder{
        private TextView mTitleText;
        private TextView mDateText;
        private TextView mNumText;
    }
}
