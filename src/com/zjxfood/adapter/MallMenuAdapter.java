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
public class MallMenuAdapter extends BaseAdapter{
    private Context mContext;
    private String[] mArrays;
    private LayoutInflater mInflater;

    public MallMenuAdapter(Context context,String[] arrays){
        this.mContext = context;
        this.mArrays = arrays;
        mInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mArrays.length;
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
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder mHolder = null;
        if(view==null){
            mHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.mall_left_menu_item,null);
            mHolder.mTextView = (TextView) view.findViewById(R.id.menu_item_text);
            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }
        mHolder.mTextView.setText(mArrays[position]);
        return view;
    }

    class ViewHolder{
        TextView mTextView;
    }
}
