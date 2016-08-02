package com.zjxfood.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.zjxfood.activity.R;
import com.zjxfood.interfaces.CarsListImpl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/2.
 */
public class CarsAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<HashMap<String, Object>> mLists;
    private BitmapUtils mBitmapUtils;
    private CarsListImpl mListImpl;
    public CarsAdapter(Context context, ArrayList<HashMap<String, Object>> list, CarsListImpl carsListImpl){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mLists = list;
        Log.i("初始化","====================="+mLists);
        mBitmapUtils = BitmapUtilSingle.getBitmapInstance(mContext);
        mListImpl = carsListImpl;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder = null;
//        view = null;
        if(view==null){
            mHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.cars_list_item,null);
            mHolder.checkBox = (CheckBox) view.findViewById(R.id.cars_check);
            mHolder.imageView = (ImageView) view.findViewById(R.id.cars_image);
            mHolder.nameText = (TextView) view.findViewById(R.id.cars_name);
            mHolder.guigeText = (TextView) view.findViewById(R.id.cars_item_guige_value);
            mHolder.priceText = (TextView) view.findViewById(R.id.cars_item_price_value);
            mHolder.numText = (TextView) view.findViewById(R.id.cars_item_view);
            mHolder.jianImage = (ImageView) view.findViewById(R.id.cars_item_jian);
            mHolder.jiaImage = (ImageView) view.findViewById(R.id.cars_item_jia);
            mHolder.mCheckImage = (ImageView) view.findViewById(R.id.cars_check2);

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
//        Log.i("刷新了view","====================="+mLists);
        if(mHolder.checkBox.isChecked()) {
            mHolder.jiaImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int a = Integer.parseInt(mLists.get(i).get("quantity").toString());
                    a++;
                    mLists.get(i).put("quantity", a);
                    notifyDataSetChanged();

                    mListImpl.notifyList(mLists.get(i), a + "", "change", i, mLists);
                }
            });
            mHolder.jianImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int a = Integer.parseInt(mLists.get(i).get("quantity").toString());
                    a--;
                    if (a <= 0) {
                        mListImpl.notifyList(mLists.get(i),mLists.get(i).get("AttrId").toString(), "remove", i, mLists);
//                        mLists.remove(i);
                        notifyDataSetChanged();
                    } else {
                        mLists.get(i).put("quantity", a);
                        notifyDataSetChanged();
                        mListImpl.notifyList(mLists.get(i), a + "", "change", i, mLists);
                    }
                }
            });
        }
        mHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mListImpl.notifyList(null,"","check+",i,null);
                }else {
                    mListImpl.notifyList(null,"","check-",i,null);
                }
            }
        });
//        final ViewHolder finalMHolder = mHolder;
//        mHolder.mCheckImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mLists.get(i).get("isCheck").toString().equals("true")){
//                    finalMHolder.mCheckImage.setImageResource(R.drawable.car_select);
////                    mLists.get(i).put("isCheck","false");
//                    mListImpl.notifyList(mLists.get(i),"","check-",i,null);
//                }else{
//                    finalMHolder.mCheckImage.setImageResource(R.drawable.car_selected);
////                    mLists.get(i).put("isCheck","true");
//                    mListImpl.notifyList(mLists.get(i),"","check+",i,null);
//                }
//            }
//        });
        return view;
    }
    class ViewHolder{
        CheckBox checkBox;
        ImageView imageView;
        TextView nameText;
        TextView guigeText;
        TextView priceText;
        TextView numText;
        ImageView jianImage;
        ImageView jiaImage;
        ImageView mCheckImage;
    }
}
