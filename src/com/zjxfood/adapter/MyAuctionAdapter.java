package com.zjxfood.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAuctionAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private LayoutInflater mInflater;
	private BitmapUtils mBitmapUtils;
	

	public MyAuctionAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(mContext);
		mBitmapUtils = BitmapUtilSingle
				.getBitmapInstance(mContext);
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
	public void notifyList(ArrayList<HashMap<String, Object>> list){
		this.mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.my_auction_list_item, null);
			mHolder = new ViewHolder();
			mHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.my_auction_item_image);
			mHolder.mName = (TextView) convertView
					.findViewById(R.id.my_auction_item_name);
			mHolder.mPrice = (TextView) convertView
					.findViewById(R.id.my_auction_item_price);
			mHolder.mButton = (Button) convertView.findViewById(R.id.my_auction_item_right_buy);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if (!Constants.isNull(mList.get(position).get("ProductName"))) {
			mHolder.mName.setText(mList.get(position).get("ProductName")
					.toString());
		}
		if (!Constants.isNull(mList.get(position).get("Price"))) {
			mHolder.mPrice.setText(Float.parseFloat(mList.get(position).get("Price")
					.toString())+"币");
		}
		if(mList.get(position).get("UserId")!=null){
			if(mList.get(position).get("UserId").toString().equals(Constants.mId)){
				mHolder.mButton.setBackgroundResource(R.drawable.bg_balance_pay_style);
			}
		}
		if (!Constants.isNull(mList.get(position).get("ImgUrl"))) {
			mBitmapUtils.display(mHolder.mImageView, mList.get(position).get("ImgUrl").toString());
		}
		return convertView;
	}

	class ViewHolder {
		private ImageView mImageView;
		private TextView mName;
		private TextView mPrice;
		private Button mButton;
	}
}
