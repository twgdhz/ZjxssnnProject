package com.zjxfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.zjxfood.activity.MallDetailActivity;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;
import com.zjxfood.interfaces.FavoriteFaceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavoriteListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private FavoriteFaceImpl mFaceImpl;
	// private ArrayList<String> mChoseList;
	private HashMap<String, String> mChoseMap;
	private BitmapUtils mBitmapUtils;
	private Map<Integer,Integer> selected;
	
	public FavoriteListAdapter(Context context,
			ArrayList<HashMap<String, Object>> list, FavoriteFaceImpl faceImpl) {
		this.mContext = context;
		this.mList = list;
		this.mFaceImpl = faceImpl;
		mChoseMap = new HashMap<String, String>();
		mBitmapUtils = BitmapUtilSingle.getBitmapInstance(mContext);
		mBitmapUtils.configDefaultLoadFailedImage(R.drawable.mall_occupying);
		selected=new HashMap<Integer,Integer>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public void notifyList(ArrayList<HashMap<String, Object>> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	public void choseAll(){
		for(int i=0;i<mList.size();i++){
			selected.put(i, i);
		}
		notifyDataSetChanged();
	}
	public void cancelAll(){
		selected.clear();
		notifyDataSetChanged();
	}
	public void delete(){
		mList.clear();
		notifyDataSetChanged();
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		final ViewHolder mHolder;
		convertView = inflater.inflate(R.layout.my_favorite_list_item, null);
		mHolder = new ViewHolder();
		mHolder.mCheckBox = (CheckBox) convertView
				.findViewById(R.id.favorite_list_item_check_image);
		mHolder.mImageView = (ImageView) convertView
				.findViewById(R.id.favorite_list_item_mall_image);
		mHolder.mMoneyText = (TextView) convertView
				.findViewById(R.id.favorite_list_item_money);
		mHolder.mPriceText = (TextView) convertView
				.findViewById(R.id.favorite_list_item_price);
		mHolder.mNameText = (TextView) convertView
				.findViewById(R.id.favorite_list_item_content_text);
		mHolder.mContentLayout = (LinearLayout) convertView.findViewById(R.id.favorite_list_item_content_layout);
		if (!Constants.isNull(mList.get(position).get("title"))) {
			mHolder.mNameText.setText(mList.get(position).get("title")
					.toString());
		}
		if(mList.get(position).get("GiftType")!=null && mList.get(position).get("price")!=null){
			Log.i("tag",mList.get(position).get("GiftType")+"========================"+mList.get(position).get("price"));
			if(mList.get(position).get("GiftType").toString().equals("3")){
				mHolder.mPriceText.setText("售价：￥"
						+ mList.get(position).get("price").toString());
			}else if(mList.get(position).get("GiftType").toString().equals("1")){
				mHolder.mPriceText.setText("售价："
						+ mList.get(position).get("price").toString()+"食尚币");
			}else if(mList.get(position).get("GiftType").toString().equals("2")){
				mHolder.mPriceText.setText("售价：￥"
						+ mList.get(position).get("price").toString());
			}
		}
//		if (!Constants.isNull(mList.get(position).get("price"))) {
//			mHolder.mPriceText.setText("食尚币："
//					+ mList.get(position).get("price").toString());
//		}
		if (!Constants.isNull(mList.get(position).get("money"))) {
			mHolder.mMoneyText.setText("市场价：￥"
					+ mList.get(position).get("money").toString());
		}
		if (!Constants.isNull(mList.get(position).get("image"))) {
			mBitmapUtils.display(mHolder.mImageView,
					mList.get(position).get("image").toString());
		}
		mHolder.mCheckBox.setTag(position);
        if(selected.containsKey(position))
        	mHolder.mCheckBox.setChecked(true);
        else
        	mHolder.mCheckBox.setChecked(false);
        addListener(mHolder,position);//添加事件响应
        
        mHolder.mContentLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mList.size() > position) {
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("flag", "1");
					if(mList.get(position).get("id")!=null){
						bundle.putString("Id", mList.get(position).get("id")
								.toString());
					}
					if(mList.get(position).get("title")!=null){
						bundle.putString("Title", mList.get(position).get("title")
								.toString());
					}
					if(mList.get(position).get("price")!=null){
						bundle.putString("Price", mList.get(position).get("price")
								.toString());
					}
					if(mList.get(position).get("yf")!=null){
						bundle.putString("Yf", mList.get(position).get("yf")
								.toString());
					}
					if(mList.get(position).get("dhnumber")!=null){
						bundle.putString("Dhnumber",
								mList.get(position).get("dhnumber").toString());
					}
					if(mList.get(position).get("image")!=null){
						bundle.putString("Image1", mList.get(position).get("image")
								.toString());
					}
					if (mList.get(position).get("image1") != null) {
						bundle.putString("titleImage1", mList.get(position)
								.get("image1").toString());
					} else {
						bundle.putString("titleImage1", "");
					}
					if (mList.get(position).get("image2") != null) {
						bundle.putString("titleImage2", mList.get(position)
								.get("image2").toString());
					} else {
						bundle.putString("titleImage2", "");
					}
					if (mList.get(position).get("image3") != null) {
						bundle.putString("titleImage3", mList.get(position)
								.get("image3").toString());
					} else {
						bundle.putString("titleImage3", "");
					}
					if(mList.get(position).get("salenumber")!=null){
						bundle.putString("sale",
								mList.get(position).get("salenumber").toString());
					}
					if(mList.get(position).get("money")!=null){
						bundle.putString("money", mList.get(position).get("money")
								.toString());
					}
					if(mList.get(position).get("GiftType")!=null){
						if(mList.get(position).get("GiftType").toString().equals("1")){
							bundle.putString("typeName", "ssb");
						}else if(mList.get(position).get("GiftType").toString().equals("2")){
							bundle.putString("typeName", "ssjb");
						}else if(mList.get(position).get("GiftType").toString().equals("3")){
							bundle.putString("typeName", "xj");
						}
					}

					if(mList.get(position).get("id")!=null){
						bundle.putString("giftId", mList.get(position).get("id")
								.toString());
					}
					if (mList.get(position).get("content") != null) {
						bundle.putString("Content",
								mList.get(position).get("content").toString());
					}
					intent.putExtras(bundle);
					intent.setClass(mContext, MallDetailActivity.class);
//					intent.setClass(mContext, MallNewDetailActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				}
			}
		});
		return convertView;
	}
    private void addListener(ViewHolder holder,final int position){
        holder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
              @Override
              public void onCheckedChanged(CompoundButton buttonView,
                          boolean isChecked) {
                    if(isChecked){
                       if(!selected.containsKey(buttonView.getTag()))
                           selected.put((Integer) buttonView.getTag(),position);
                       if (!Constants
								.isNull(mList.get(position).get("id"))) {
							mChoseMap.put(position + "", mList
									.get(position).get("id").toString());
							
						}
                    }
                    else{
                          selected.remove((Integer) buttonView.getTag());
                          if (mChoseMap != null
									&& mChoseMap.get(position + "") != null) {
								mChoseMap.remove(position + "");
							}
                    }
                    mFaceImpl.doList(mChoseMap);
              }
        });
    }
	class ViewHolder {
		private CheckBox mCheckBox;
		private ImageView mImageView;
		private TextView mNameText;
		private TextView mPriceText;// 食尚币
		private TextView mMoneyText;// 市场价
		private LinearLayout mContentLayout;
	}
}
