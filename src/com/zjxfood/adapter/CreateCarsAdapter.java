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
 * Created by Administrator on 2016/7/28.
 */
public class CreateCarsAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<HashMap<String,Object>> mLists;
    private BitmapUtils mBitmapUtils;
    public CreateCarsAdapter(Context context,ArrayList<HashMap<String,Object>> list){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mLists = list;
        mBitmapUtils = BitmapUtilSingle.getBitmapInstance(mContext);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder mHolder = null;
        if(view==null){
            mHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.cars_create_order_item,null);
            mHolder.guigeText = (TextView) view.findViewById(R.id.cars_item_guige_value);
            mHolder.imageView = (ImageView) view.findViewById(R.id.create_order_image);
            mHolder.nameText = (TextView) view.findViewById(R.id.create_order_name);
            mHolder.numText = (TextView) view.findViewById(R.id.create_order_num);
            mHolder.priceText = (TextView) view.findViewById(R.id.create_order_price);
            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }
        if(mLists.get(i).get("ProductName")!=null){
            mHolder.nameText.setText(mLists.get(i).get("ProductName").toString());
        }
        if(mLists.get(i).get("chima")!=null){
            mHolder.guigeText.setText(mLists.get(i).get("chima").toString());
        }
        if(mLists.get(i).get("Price")!=null){
            mHolder.priceText.setText("￥"+mLists.get(i).get("Price").toString());
        }
        if(mLists.get(i).get("Image")!=null){
            mBitmapUtils.display(mHolder.imageView,mLists.get(i).get("Image").toString());
        }
        if(mLists.get(i).get("quantity")!=null) {
            mHolder.numText.setText(mLists.get(i).get("quantity").toString());
        }
        return view;
    }

    class ViewHolder{
        ImageView imageView;
        TextView nameText;
        TextView guigeText;
        TextView priceText;
        TextView numText;
    }
}
