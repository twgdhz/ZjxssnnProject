package com.zjxfood.adapter;

import com.zjxfood.activity.R;
import com.zjxfood.adapter.AddressManListAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MerchantSettledListAdapter extends BaseAdapter{

	private Context mContext;
	private String[] mList;
	private LayoutInflater mInflater;
	public MerchantSettledListAdapter(Context context,String[] list){
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(mContext);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.length;
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder mHolder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.new_address_popup_list_item, null);
			mHolder = new ViewHolder();
			
			mHolder.mTextView = (TextView) convertView.findViewById(R.id.new_address_popup_item);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.mTextView.setText(mList[position]);
		return convertView;
	}
	
	class ViewHolder{
		TextView mTextView;
	}
}
