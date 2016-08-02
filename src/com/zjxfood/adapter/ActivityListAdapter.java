package com.zjxfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.project.util.DensityUtils;
import com.zjxfood.activity.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/16.
 */
public class ActivityListAdapter extends BaseAdapter{

    private ArrayList<HashMap<String,Object>> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private BitmapUtils mBitmapUtils;
    public ActivityListAdapter(Context context,ArrayList<HashMap<String,Object>> list){
        this.mList = list;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mBitmapUtils = BitmapUtilSingle
                .getBitmapInstance(mContext);
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
            view = mInflater.inflate(R.layout.activity_list_item,null);

            mHolder.imageView = (ImageView) view.findViewById(R.id.act_item_image);
            ViewGroup.LayoutParams params = mHolder.imageView.getLayoutParams();
            params.height = DensityUtils.dp2px(mContext,120);
            mHolder.imageView.setLayoutParams(params);
            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }
        if(mList.get(i).get("Images")!=null){
            mBitmapUtils.display(mHolder.imageView,mList.get(i).get("Images").toString());
        }
        return view;
    }

    class ViewHolder{
        ImageView imageView;
    }
}
