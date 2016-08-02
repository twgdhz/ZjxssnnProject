package com.zjxfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zjxfood.activity.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/3.
 */
public class MallNewOrderAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<HashMap<String,Object>> mList;
    private BitmapUtils mBitmapUtils;

    public MallNewOrderAdapter(Context context,ArrayList<HashMap<String,Object>> list){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mList = list;
        mBitmapUtils = new BitmapUtils(mContext);
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder mHolder = null;
        if(view==null){
            mHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.mall_new_order_list_item,null);

            mHolder.imageView = (ImageView) view.findViewById(R.id.mall_item_image);
            mHolder.name = (TextView) view.findViewById(R.id.mall_item_name);
            mHolder.price = (TextView) view.findViewById(R.id.mall_item_price);
            mHolder.num = (TextView) view.findViewById(R.id.mall_item_num);

            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }
        if(mList.get(position).get("title")!=null){
            mHolder.name.setText(mList.get(position).get("title").toString());
        }
        if(mList.get(position).get("price")!=null){
            mHolder.price.setText("价格："+mList.get(position).get("price").toString()+"食尚币");
        }
        if(mList.get(position).get("giftAvatar")!=null){
                mBitmapUtils.display(mHolder.imageView, mList.get(position).get("giftAvatar").toString());

        }

        return view;
    }

    class ViewHolder{
        ImageView imageView;
        TextView name;
        TextView price;
        TextView num;
    }
}
