package com.zjxfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjxfood.activity.R;

/**
 * Created by Administrator on 2016/6/12.
 */
public class MallChoseTypeAdapter extends BaseAdapter{
    private Context mContext;
    private String[] mList;
    private LayoutInflater mInflater;
    public MallChoseTypeAdapter(Context context,String[] list){
        this.mContext = context;
        this.mList = list;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.length;
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
            view = mInflater.inflate(R.layout.mall_chose_type_list_item,null);
            mHolder.mText = (TextView) view.findViewById(R.id.mall_type_item);
            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }
        if(mList[i]!=null){
            mHolder.mText.setText(mList[i]);
        }
        return view;
    }
    class ViewHolder{
        TextView mText;
    }
}
