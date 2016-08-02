package com.zjxfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.zjxfood.activity.MallDetailActivity;
import com.zjxfood.activity.R;

import java.util.ArrayList;
import java.util.HashMap;

public class NewJbMallGridAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private LayoutInflater mInflater;
	private BitmapUtils mBitmapUtils;
	private String mTypeName;

	public NewJbMallGridAdapter(Context context,
								ArrayList<HashMap<String, Object>> list, String typeName) {
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(mContext);
		this.mTypeName = typeName;
//		mBitmapUtils = new BitmapUtils(mContext);
		mBitmapUtils = BitmapUtilSingle.getBitmapInstance(mContext);
		mBitmapUtils.configDefaultLoadFailedImage(R.drawable.mall_occupying);
//		mBitmapUtils.configDefaultLoadingImage(drawable)
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void notify(ArrayList<HashMap<String, Object>> list) {
		this.mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.new_jb_mall_item, null);
			mHolder = new ViewHolder();
			mHolder.mNameText = (TextView) convertView.findViewById(R.id.new_jb_item_name);
			mHolder.mSaleText = (TextView) convertView.findViewById(R.id.new_jb_item_sale);
			mHolder.mScjText = (TextView) convertView.findViewById(R.id.new_jb_item_scj_value);
			mHolder.mImageView1 = (ImageView) convertView.findViewById(R.id.new_jb_item_image);
			mHolder.mDzText = (TextView) convertView.findViewById(R.id.new_jb_item_dk);
			mHolder.mLayout1 = (LinearLayout) convertView.findViewById(R.id.new_jb_layout);
//			mHolder.mDzlayout = (FrameLayout) convertView.findViewById(R.id.jb_dz_layout);
//			mHolder.mDkImage = (ImageView) convertView.findViewById(R.id.mall_dk_image);
//			ViewGroup.LayoutParams params = mHolder.mDkImage.getLayoutParams();
//			params.height = (int) (ScreenUtils.getScreenWidth(mContext)*0.2);
//			params.width = (int) (ScreenUtils.getScreenWidth(mContext)*0.2);
//			mHolder.mDkImage.setLayoutParams(params);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		if (mList.size() > position) {

			if (mList.get(position).get("Image") != null) {
				mBitmapUtils.display(mHolder.mImageView1, mList.get(position)
						.get("Image").toString());
			}
			mBitmapUtils.configDefaultBitmapMaxSize(250, 250);
			if(mList.get(position).get("Title")!=null){
			mHolder.mNameText.setText(mList.get(position).get("Title")
					.toString());
			}
			if(mList.get(position).get("Money")!=null){
				Double price = Double.parseDouble(mList.get(position).get("Money")
						.toString());
				mHolder.mScjText.setText("￥"+ new java.text.DecimalFormat("#.00").format(price));
				mHolder.mScjText.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
			}

			if(mList.get(position).get("BuyingPrice")!=null){
			Double money = Double.parseDouble(mList.get(position).get("BuyingPrice")
					.toString());
				mHolder.mSaleText.setText("￥"
						+ new java.text.DecimalFormat("#.00").format(money));

			}
			if(mList.get(position).get("GoldMoneyBLDisplay")!=null){

					mHolder.mDzText.setText(mList.get(position).get("GoldMoneyBLDisplay") + "%");
			}
		}

		mHolder.mLayout1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mList!=null && mList.size() > position) {
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("flag", "1");
					if(mList.get(position).get("Id")!=null){
					bundle.putString("Id", mList.get(position).get("Id")
							.toString());
					}
					if(mList.get(position).get("Title")!=null){
					bundle.putString("Title", mList.get(position).get("Title")
							.toString());
					}
					if(mList.get(position).get("BuyingPrice")!=null){
					bundle.putString("Price", mList.get(position).get("BuyingPrice")
							.toString());
					}
					if(mList.get(position).get("Yf")!=null){
					bundle.putString("Yf", mList.get(position).get("Yf")
							.toString());
					}
					if(mList.get(position).get("Dhnumber")!=null){
					bundle.putString("Dhnumber",
							mList.get(position).get("Dhnumber").toString());
					}
					if(mList.get(position).get("Image")!=null){
					bundle.putString("Image1", mList.get(position).get("Image")
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
					if(mList.get(position).get("Salenumber")!=null){
					bundle.putString("sale",
							mList.get(position).get("Salenumber").toString());
					}
					if(mList.get(position).get("Money")!=null){
					bundle.putString("money", mList.get(position).get("Money")
							.toString());
					}
					bundle.putString("typeName", mTypeName);
					if(mList.get(position).get("Id")!=null){
					bundle.putString("giftId", mList.get(position).get("Id")
							.toString());
					}
					if (mList.get(position).get("Content") != null) {
						bundle.putString("Content",
								mList.get(position).get("Content").toString());
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

	class ViewHolder {
		ImageView mImageView1;
		TextView mNameText;
		TextView mSaleText;
		TextView mScjText;
		TextView mDzText;
		LinearLayout mLayout1;
//		FrameLayout mDzlayout;
//		ImageView mDkImage;
	}

}
