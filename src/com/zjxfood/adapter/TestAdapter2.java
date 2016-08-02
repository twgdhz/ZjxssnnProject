package com.zjxfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjxfood.activity.R;

/**
 * Created by Administrator on 2016/6/2.
 */
public class TestAdapter2 extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    public TestAdapter2(Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return 2;
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
            view = mInflater.inflate(R.layout.text_list_item4,null);
            mHolder.textView = (TextView) view.findViewById(R.id.test_list_item2_text);
            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }
        return view;
    }
    class ViewHolder{
        TextView textView;
    }
}
