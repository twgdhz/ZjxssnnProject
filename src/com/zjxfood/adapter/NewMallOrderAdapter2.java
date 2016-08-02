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
 * Created by Administrator on 2016/7/25.
 */
public class NewMallOrderAdapter2 extends BaseAdapter{
    private Context mContext;
    private ArrayList<HashMap<String,Object>> mLists;
    private LayoutInflater mInflater;
    private BitmapUtils mBitmapUtils;
    public NewMallOrderAdapter2(Context context,ArrayList<HashMap<String,Object>> list){
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
            view = mInflater.inflate(R.layout.new_mall_order_item,null);
            mHolder.imageView = (ImageView) view.findViewById(R.id.create_order2_image);
            mHolder.nameText = (TextView) view.findViewById(R.id.create_order2_name);
            mHolder.numText = (TextView) view.findViewById(R.id.new_mall_item_num);
            mHolder.priceText = (TextView) view.findViewById(R.id.new_mall_item_price_value);
            mHolder.shuxingText = (TextView) view.findViewById(R.id.create_order2_shuxing);

            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }
        if(mLists.get(i).get("ProductName")!=null){
            mHolder.nameText.setText(mLists.get(i).get("ProductName").toString());
        }
        if(mLists.get(i).get("KCText")!=null){
            mHolder.shuxingText.setText("规格属性："+mLists.get(i).get("KCText").toString());
        }
        if(mLists.get(i).get("ProductPrice")!=null){
            mHolder.priceText.setText("￥"+mLists.get(i).get("ProductPrice").toString());
        }
        if(mLists.get(i).get("Quantity")!=null){
            mHolder.numText.setText("x"+mLists.get(i).get("Quantity").toString());
        }
        if(mLists.get(i).get("ProductImage")!=null){
            mBitmapUtils.display(mHolder.imageView,mLists.get(i).get("ProductImage").toString());
        }
        return view;
    }
    class ViewHolder{
        ImageView imageView;
        TextView nameText;
        TextView shuxingText;
        TextView priceText;
        TextView numText;
    }
}
