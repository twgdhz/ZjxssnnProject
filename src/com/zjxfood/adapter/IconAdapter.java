package com.zjxfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.zjxfood.activity.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/15.
 */
public class IconAdapter extends BaseAdapter{
    private ArrayList<HashMap<String,Object>> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private BitmapUtils mBitmapUtils;

    public IconAdapter(Context context,ArrayList<HashMap<String,Object>> list){
        this.mContext = context;
        this.mList = list;
        mInflater = LayoutInflater.from(mContext);
        mBitmapUtils = BitmapUtilSingle.getBitmapInstance(mContext);
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
            view = mInflater.inflate(R.layout.icon_list_item,null);
            mHolder.imageView = (ImageView) view.findViewById(R.id.icon_image);
            mHolder.textView = (TextView) view.findViewById(R.id.icon_text);
            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }
        if(mList.get(i).get("name")!=null){
            mHolder.textView.setText(mList.get(i).get("name").toString());
        }
        if(mList.get(i).get("iconUrl")!=null){
            mBitmapUtils.display(mHolder.imageView,mList.get(i).get("iconUrl").toString());
        }
        return view;
    }
    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
