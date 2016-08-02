package com.zjxfood.adapter;

import com.zjxfood.activity.R;
import com.zjxfood.adapter.AddressManListAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AuctionOrderValueAdapter extends BaseAdapter{

	private Context mContext;
	public AuctionOrderValueAdapter(Context context){
		this.mContext = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.auction_order_value_list_item, null);
			mHolder = new ViewHolder();
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}
	class ViewHolder{
		private TextView mName;
		private TextView mModel;
		private TextView mScrenn;
		private TextView mMemory;
		private TextView mNumbers;
		private TextView mColor;
		private TextView mPrice;
		private TextView mOrder;
		private TextView mDate;
	}

	
}
