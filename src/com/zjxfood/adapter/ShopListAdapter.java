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

public class ShopListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private LayoutInflater mInflater;
	private BitmapUtils mBitmapUtils;

	public ShopListAdapter(Context context,
						   ArrayList<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(mContext);
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
			convertView = mInflater.inflate(R.layout.mall_list_grid_item, null);
			mHolder = new ViewHolder();
			mHolder.mImageView1 = (ImageView) convertView
					.findViewById(R.id.sign_item_commodity_image);

			mHolder.mTextView1 = (TextView) convertView
					.findViewById(R.id.sign_item_new_commodity_name);

			mHolder.mLayout1 = (LinearLayout) convertView
					.findViewById(R.id.sign_item_new_commodity_layout);

			mHolder.mSale = (TextView) convertView
					.findViewById(R.id.sign_item_commodity_bi);

			mHolder.mMarketPrice1 = (TextView) convertView
					.findViewById(R.id.mall_new_list_price_volume);
			mHolder.mScjText = (TextView) convertView.findViewById(R.id.mall_new_list_scj_volume);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		if (mList.size() > position) {
			if (mList.get(position).get("image") != null) {
				mBitmapUtils.display(mHolder.mImageView1, mList.get(position)
						.get("image").toString());
			}
			mBitmapUtils.configDefaultBitmapMaxSize(250, 250);
			if(mList.get(position).get("title")!=null){
			mHolder.mTextView1.setText(mList.get(position).get("title")
					.toString());
			}
			if(mList.get(position).get("money")!=null){
				Double price = Double.parseDouble(mList.get(position).get("money")
						.toString());
				mHolder.mScjText.setText("市场价：￥"+ new java.text.DecimalFormat("#.00").format(price));
				mHolder.mScjText.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
			}

			if(mList.get(position).get("price")!=null){
			Double money = Double.parseDouble(mList.get(position).get("price")
					.toString());
			mHolder.mMarketPrice1.setText("售价：￥"
					+ new java.text.DecimalFormat("#.00").format(money));
			}
			if(mList.get(position).get("salenumber")!=null){
				Double price = Double.parseDouble(mList.get(position).get("salenumber")
						.toString());
				mHolder.mSale.setText("销量："+ new java.text.DecimalFormat("#.00").format(price));
			}
		}

		mHolder.mLayout1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mList!=null && mList.size() > position) {
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

	class ViewHolder {
		ImageView mImageView1;
		TextView mTextView1;
		TextView mSale;
		LinearLayout mLayout1;
		TextView mMarketPrice1;
		private TextView mScjText;
	}

}
