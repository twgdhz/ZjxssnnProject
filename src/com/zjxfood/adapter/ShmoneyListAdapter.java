package com.zjxfood.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjxfood.activity.R;

public class ShmoneyListAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	public ShmoneyListAdapter(Context context,ArrayList<HashMap<String, Object>> list){
		this.mContext = context;
		this.mList = list;
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
		this.mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.my_restaurant_list_item, null);
			mHolder = new ViewHolder();
			mHolder.mTitleText = (TextView) convertView.findViewById(R.id.my_bill_item_text_title);
			mHolder.mDateText = (TextView) convertView.findViewById(R.id.my_bill_item_text_date);
			mHolder.mNumText = (TextView) convertView.findViewById(R.id.my_bill_item_text_money);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		mHolder.mTitleText.setText(mList.get(position).get("Infomes").toString());
		mHolder.mDateText.setText(mList.get(position).get("Createtime").toString());
		mHolder.mNumText.setText(mList.get(position).get("Money").toString());
		return convertView;
	}

	class ViewHolder{
		private TextView mTitleText;
		private TextView mDateText;
		private TextView mNumText;
	}
}
