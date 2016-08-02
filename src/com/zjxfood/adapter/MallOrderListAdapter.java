package com.zjxfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zjxfood.activity.LogisticsActivity;
import com.zjxfood.activity.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MallOrderListAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private BitmapUtils mBitmapUtils;
	
	public MallOrderListAdapter(Context context,ArrayList<HashMap<String, Object>> list){
		this.mContext = context;
		this.mList = list;
		mBitmapUtils = new BitmapUtils(mContext);
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
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void nitify(ArrayList<HashMap<String, Object>> list){
		this.mList = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.mall_order_list_item, null);
			mHolder = new ViewHolder();
			mHolder.mImageView = (ImageView) convertView.findViewById(R.id.mall_order_item_image);
			mHolder.mName = (TextView) convertView.findViewById(R.id.mall_item_name);
			mHolder.mStatus = (TextView) convertView.findViewById(R.id.mall_item_deliver_goods_status);
			mHolder.mPrice = (TextView) convertView.findViewById(R.id.mall_item_price);
			mHolder.mAddress = (TextView) convertView.findViewById(R.id.mall_item_address);
			mHolder.mCheckLogistics = (TextView) convertView.findViewById(R.id.mall_item_logistics);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(mList.get(position).get("title")!=null){
		mHolder.mName.setText(mList.get(position).get("title").toString());
		}
		if(mList.get(position).get("price")!=null){
		mHolder.mPrice.setText("支付现金："+mList.get(position).get("price").toString());
		}
		if(mList.get(position).get("buyaddress")!=null){
		mHolder.mAddress.setText("发货地址："+mList.get(position).get("buyaddress").toString());
		}
		if(mList.get(position).get("status")!=null){
			mHolder.mStatus.setText("发货状态："+mList.get(position).get("status").toString());
		}
//		DisplayImageOptions options = new DisplayImageOptions.Builder()
//		.cacheInMemory(false).cacheOnDisc(true)
//		.showImageOnFail(R.drawable.log)
//		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//		.showImageForEmptyUri(R.drawable.log)
//		.bitmapConfig(Bitmap.Config.RGB_565).build();
		if(mList.get(position).get("giftAvatar")!=null){
			try {
//				ImageLoader.getInstance().displayImage(
//						mList.get(position).get("giftAvatar").toString(),
//						mHolder.mImageView, options);
				mBitmapUtils.display(mHolder.mImageView, mList.get(position).get("giftAvatar").toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(mList.get(position).get("status").toString().equals("已发货")){
			mHolder.mCheckLogistics.setVisibility(View.VISIBLE);
		}
		mHolder.mCheckLogistics.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(mList.get(position).get("fh")!=null){
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("fh", mList.get(position).get("fh").toString());
					intent.putExtras(bundle);
					intent.setClass(mContext, LogisticsActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				}
			}
		});
		return convertView;
	}
	class ViewHolder{
		private ImageView mImageView;
		private TextView mName;
		private TextView mStatus;
		private TextView mPrice;
		private TextView mAddress;
		private TextView mCheckLogistics;
	}
}
