package com.zjxfood.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;

public class MyOrderListAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private BitmapUtils mBitmapUtils;
	
	public MyOrderListAdapter(Context context,ArrayList<HashMap<String, Object>> list){
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
	
	public void notify(ArrayList<HashMap<String, Object>> list){
		this.mList = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if(convertView==null){
			mHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.merchant_order_list_item, null);
			
			mHolder.mMerchantName = (TextView) convertView.findViewById(R.id.merchant_item_name);
			mHolder.mDate = (TextView) convertView.findViewById(R.id.merchant_item_date_show);
			mHolder.mPrice = (TextView) convertView.findViewById(R.id.merchant_item_price);
			mHolder.mAddress = (TextView) convertView.findViewById(R.id.merchant_item_address);
			mHolder.mMerchantImage = (ImageView) convertView.findViewById(R.id.merchant_order_item_image);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(!Constants.isNull(mList.get(position).get("Merchantname"))){
		mHolder.mMerchantName.setText(mList.get(position).get("Merchantname").toString());
		}
		if(!Constants.isNull(mList.get(position).get("Createtime"))){
		mHolder.mDate.setText("付款时间："+mList.get(position).get("Createtime").toString());
		}
		if(!Constants.isNull(mList.get(position).get("Money"))){
		mHolder.mPrice.setText("付款金额："+mList.get(position).get("Money").toString());
		}
		if(!Constants.isNull(mList.get(position).get("Address"))){
		mHolder.mAddress.setText("商家地址："+mList.get(position).get("Address").toString());
		}
//		DisplayImageOptions options = new DisplayImageOptions.Builder()
//		.cacheInMemory(true).cacheOnDisc(true)
//		.showImageOnFail(R.drawable.log)
//		.showImageForEmptyUri(R.drawable.log)
//		.bitmapConfig(Bitmap.Config.RGB_565).build();
//		ImageLoader.getInstance().displayImage(
//				mList.get(position).get("Logoimage").toString(),
//				mHolder.mMerchantImage, options);
		if(mList.get(position).get("Logoimage")!=null){
		mBitmapUtils.display(mHolder.mMerchantImage, mList.get(position).get("Logoimage").toString());
		}
//		mHolder.mEvaluationBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(mContext, OrderEvaluationInfoActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				mContext.startActivity(intent);
//			}
//		});
		
		return convertView;
	}

	class ViewHolder{
		TextView mAddress;
		TextView mMerchantName;
		TextView mPrice;
		ImageView mMerchantImage;
		TextView mDate;
	}
}
