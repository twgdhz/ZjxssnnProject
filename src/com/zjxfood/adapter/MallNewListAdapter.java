package com.zjxfood.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.xutils.BitmapUtils;
import com.zjxfood.activity.MallDetailActivity;
import com.zjxfood.activity.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MallNewListAdapter extends BaseAdapter{

	private Context mContext;
	private int mNum;
	private ArrayList<HashMap<String, Object>> mList;
	private LayoutInflater mInflater;
	private BitmapUtils mBitmapUtils;
	public MallNewListAdapter(Context context,int num,
			ArrayList<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mNum = num;
		this.mList = list;
		mInflater = LayoutInflater.from(mContext);
		
		mBitmapUtils = new BitmapUtils(mContext);
//		mBitmapUtils.configDiskCacheEnabled(true);
//		mBitmapUtils.configMemoryCacheEnabled(false);
	}

	@Override
	public int getCount() {
		return mNum;
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
	
	public void notify(ArrayList<HashMap<String, Object>> list,int num){
//		this.mList = list;
		this.mList.addAll(list);
		this.mNum = num;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
//		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.mall_list_item, null);
			mHolder = new ViewHolder();
			mHolder.mImageView1 = (ImageView) convertView
					.findViewById(R.id.sign_item_commodity_image);
			mHolder.mImageView2 = (ImageView) convertView
					.findViewById(R.id.sign_item_commodity_image2);
			mHolder.mImageView3 = (ImageView) convertView
					.findViewById(R.id.sign_item_commodity_image3);
			mHolder.mImageView4 = (ImageView) convertView
					.findViewById(R.id.sign_item_commodity_image4);
			mHolder.mTextView1 = (TextView) convertView
					.findViewById(R.id.sign_item_new_commodity_name);
			mHolder.mTextView2 = (TextView) convertView
					.findViewById(R.id.sign_new_item_commodity_name2);
			mHolder.mTextView3 = (TextView) convertView
					.findViewById(R.id.sign_item_commodity_name3);
			mHolder.mTextView4 = (TextView) convertView
					.findViewById(R.id.sign_item_commodity_name4);
			mHolder.mLayout1 = (LinearLayout) convertView
					.findViewById(R.id.sign_item_new_commodity_layout);
			mHolder.mLayout2 = (LinearLayout) convertView
					.findViewById(R.id.sign_item_new_commodity_layout2);
			mHolder.mLayout3 = (LinearLayout) convertView
					.findViewById(R.id.sign_item_commodity_layout3);
			mHolder.mLayout4 = (LinearLayout) convertView
					.findViewById(R.id.sign_item_commodity_layout4);
			mHolder.mContentLayout1 = (LinearLayout) convertView.findViewById(R.id.sign_item_commodity_detail_layout);
			mHolder.mContentLayout2 = (LinearLayout) convertView.findViewById(R.id.sign_item_commodity_detail_layout2);
			mHolder.mView2 = convertView.findViewById(R.id.malll_list_view_2);
			
			mHolder.mPrice1 = (TextView) convertView.findViewById(R.id.sign_item_commodity_bi);
			mHolder.mPrice2 = (TextView) convertView.findViewById(R.id.sign_item_commodity_bi2);
			mHolder.mPrice3 = (TextView) convertView.findViewById(R.id.sign_item_commodity_bi3);
			mHolder.mPrice4 = (TextView) convertView.findViewById(R.id.sign_item_commodity_bi4);
			mHolder.mStock1 = (TextView) convertView.findViewById(R.id.mall_list_kuncun);
			mHolder.mStock2 = (TextView) convertView.findViewById(R.id.mall_list_kuncun2);
			mHolder.mStock3 = (TextView) convertView.findViewById(R.id.mall_list_kuncun3);
			mHolder.mStock4 = (TextView) convertView.findViewById(R.id.sign_item_commodity_stock4);
			mHolder.mSale1 = (TextView) convertView.findViewById(R.id.mall_list_sales_volume);
			mHolder.mSale2 = (TextView) convertView.findViewById(R.id.mall_list_sales_volume2);
			mHolder.mSale3 = (TextView) convertView.findViewById(R.id.mall_list_sales_volume3);
			
			mHolder.mMarketPrice1 = (TextView) convertView.findViewById(R.id.mall_new_list_price_volume);
			mHolder.mMarketPrice2 = (TextView) convertView.findViewById(R.id.mall_new_list_price_volume2);
			mHolder.mMarketPrice3 = (TextView) convertView.findViewById(R.id.mall_list_price_volume3);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		if (mList.size() > position * 2) {
//			ImageLoader.getInstance().displayImage(mList.get(position*2).get("image").toString(), mHolder.mImageView1);
			mBitmapUtils.display(mHolder.mImageView1, mList.get(position*2).get("image").toString());
			mHolder.mTextView1.setText(mList.get(position * 2).get("title")
					.toString());
			Double Yf = Double.parseDouble(mList.get(position*2).get("price").toString());
			mHolder.mPrice1.setText("食尚币："+new java.text.DecimalFormat("#.00").format(Yf));
//			mHolder.mStock1.setText("当前库存："+mList.get(position * 3).get("dhnumber").toString());
			mHolder.mSale1.setText("销量:"+mList.get(position * 2).get("salenumber").toString());
			mHolder.mMarketPrice1.setText("市场价:"+mList.get(position * 2).get("money").toString());
		}else{
//			mHolder.mImageView1.setImageResource(R.drawable.log);
		}
		if (mList.size() > position * 2 + 1) {
//			ImageLoader.getInstance().displayImage(mList.get(position*2+1).get("image").toString(), mHolder.mImageView2);
			mBitmapUtils.display(mHolder.mImageView2, mList.get(position*2+1).get("image").toString());
			mHolder.mTextView2.setText(mList.get(position * 2 + 1).get("title")
					.toString());
			Double Yf = Double.parseDouble(mList.get(position*2+1).get("price").toString());
			mHolder.mPrice2.setText("食尚币："+new java.text.DecimalFormat("#.00").format(Yf));
//			mHolder.mStock2.setText("当前库存："+mList.get(position * 3+1).get("dhnumber").toString());
			mHolder.mSale2.setText("销量:"+mList.get(position * 2+1).get("salenumber").toString());
			mHolder.mMarketPrice2.setText("市场价:"+mList.get(position * 2+1).get("money").toString());
		}else{
			mHolder.mContentLayout2.setBackgroundResource(R.color.my_order_line_color);
			mHolder.mView2.setVisibility(View.GONE);
		}

		mHolder.mLayout1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(mList.size() > position * 2){
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("flag", "1");
				bundle.putString("Id", mList.get(position*2).get("id").toString());
				bundle.putString("Title", mList.get(position*2).get("title").toString());
				bundle.putString("Price", mList.get(position*2).get("price").toString());
				bundle.putString("Yf", mList.get(position*2).get("yf").toString());
				bundle.putString("Dhnumber", mList.get(position*2).get("dhnumber").toString());
				bundle.putString("Image1", mList.get(position*2).get("image").toString());
				if(mList.get(position*2).get("image1")!=null){
					bundle.putString("titleImage1", mList.get(position*2).get("image1").toString());
				}else{
					bundle.putString("titleImage1", "");
				}
				if(mList.get(position*2).get("image2")!=null){
					bundle.putString("titleImage2", mList.get(position*2).get("image2").toString());
				}else{
					bundle.putString("titleImage2", "");
				}
				if(mList.get(position*2).get("image3")!=null){
					bundle.putString("titleImage3", mList.get(position*2).get("image3").toString());
				}else{
					bundle.putString("titleImage3", "");
				}
				
				bundle.putString("sale", mList.get(position*2).get("salenumber").toString());
				bundle.putString("money", mList.get(position*2).get("money").toString());
				bundle.putString("giftId", mList.get(position*2).get("id").toString());
				if(mList.get(position*2).get("content")!=null){
				bundle.putString("Content", mList.get(position*2).get("content").toString());
				}
//				Constants.mGiftBitmap = mBitmaps[position*4];
				intent.putExtras(bundle);
				intent.setClass(mContext, MallDetailActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
				}
			}
		});
		mHolder.mLayout2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(mList.size() > position * 2+1){
				Intent intent = new Intent();
				
				Bundle bundle = new Bundle();
				bundle.putString("flag", "1");
				bundle.putString("Id", mList.get(position*2+1).get("id").toString());
				bundle.putString("Title", mList.get(position*2+1).get("title").toString());
				bundle.putString("Price", mList.get(position*2+1).get("price").toString());
				bundle.putString("Yf", mList.get(position*2+1).get("yf").toString());
				bundle.putString("Dhnumber", mList.get(position*2+1).get("dhnumber").toString());
				bundle.putString("Image", mList.get(position*2+1).get("image").toString());
				bundle.putString("sale", mList.get(position*2+1).get("salenumber").toString());
				bundle.putString("money", mList.get(position*2+1).get("money").toString());
				bundle.putString("giftId", mList.get(position*2+1).get("id").toString());
				if(mList.get(position*2+1).get("content")!=null){
				bundle.putString("Content", mList.get(position*2+1).get("content").toString());
				}
				if(mList.get(position*2+1).get("image1")!=null){
					bundle.putString("titleImage1", mList.get(position*2+1).get("image1").toString());
				}else{
					bundle.putString("titleImage1", "");
				}
				if(mList.get(position*2+1).get("image2")!=null){
					bundle.putString("titleImage2", mList.get(position*2+1).get("image2").toString());
				}else{
					bundle.putString("titleImage2", "");
				}
				if(mList.get(position*2+1).get("image3")!=null){
					bundle.putString("titleImage3", mList.get(position*2+1).get("image3").toString());
				}else{
					bundle.putString("titleImage3", "");
				}
//				Constants.mGiftBitmap = mBitmaps[position*4];
				intent.putExtras(bundle);
//				Log.i("gift", mList.get(position*3+1).get("Image").toString()+"=====mList.get(position*4+1)======");
				intent.setClass(mContext, MallDetailActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
				}
			}
		});
		
		return convertView;
	}

	class ViewHolder {
		ImageView mImageView1;
		ImageView mImageView2;
		ImageView mImageView3;
		ImageView mImageView4;
		TextView mTextView1;
		TextView mTextView2;
		TextView mTextView3;
		TextView mTextView4;
		TextView mPrice1,mPrice2,mPrice3,mPrice4;
		TextView mStock1,mStock2,mStock3,mStock4;
		LinearLayout mLayout1, mLayout2, mLayout3, mLayout4;
		TextView mSale1;
		TextView mSale2;
		TextView mSale3;
		LinearLayout mContentLayout1,mContentLayout2;
		View mView2;
		TextView mMarketPrice1,mMarketPrice2,mMarketPrice3;
	}

}
