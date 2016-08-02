package com.zjxfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjxfood.activity.R;

/**
 * Created by Administrator on 2016/6/2.
 */
public class NewCashListPopAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private String[] mList;
    private int mPosition;

    public NewCashListPopAdapter(Context context,String[] list,int position){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mList = list;
        mPosition = position;
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

//    public void selectPosition(int position){
//        mPosition = position;
//        notifyDataSetChanged();
//    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder = null;
        if(view==null){
            mHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.new_cash_list_popup_item,null);
            mHolder.textView = (TextView) view.findViewById(R.id.new_cash_popup_item);
            mHolder.mCheckImage = (ImageView) view.findViewById(R.id.new_cash_popup_image);

            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }
        if(mPosition == i){
            mHolder.mCheckImage.setVisibility(View.VISIBLE);
            mHolder.textView.setTextColor(mContext.getResources().getColor(R.color.main_color16));
        }else {
            mHolder.mCheckImage.setVisibility(View.GONE);
            mHolder.textView.setTextColor(mContext.getResources().getColor(R.color.main_color3));
        }
        mHolder.textView.setText(mList[i]);
        return view;
    }
    class ViewHolder{
        TextView textView;
        ImageView mCheckImage;
    }
}
