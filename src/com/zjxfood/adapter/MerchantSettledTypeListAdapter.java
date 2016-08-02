package com.zjxfood.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.zjxfood.activity.R;
import com.zjxfood.adapter.AddressManListAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MerchantSettledTypeListAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private LayoutInflater mInflater;
	public MerchantSettledTypeListAdapter(Context context,ArrayList<HashMap<String, Object>> list){
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(mContext);
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
		mHolder.mTextView.setText(mList.get(position).get("name").toString());
		return convertView;
	}
	
	class ViewHolder{
		TextView mTextView;
	}
}
