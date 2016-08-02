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
 * Created by Administrator on 2016/6/2.
 */
public class MallThreeAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<HashMap<String,Object>> mList;
    public MallThreeAdapter(Context context,ArrayList<HashMap<String,Object>> list){
        this.mContext = context;
        this.mList = list;
        mInflater = LayoutInflater.from(mContext);
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
        if(mList.get(i).get("name")!=null){
            mHolder.textView.setText(mList.get(i).get("name").toString());
        }
        return view;
    }
    class ViewHolder{
        TextView textView;
    }
}
