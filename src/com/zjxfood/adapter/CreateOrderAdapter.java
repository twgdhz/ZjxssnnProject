package com.zjxfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zjxfood.activity.R;

/**
 * Created by Administrator on 2016/6/3.
 */
public class CreateOrderAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    public CreateOrderAdapter(Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return 5;
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
            view = mInflater.inflate(R.layout.create_order_item,null);

            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }
        return view;
    }

    class ViewHolder{

    }
}
