package com.zjxfood.indiana;

import java.util.ArrayList;
import java.util.HashMap;

import com.project.util.Utils;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;
import com.zjxfood.indiana.IndianaListAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyIndianaInfoAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private LayoutInflater mInflater;
	private String mNum;
	public MyIndianaInfoAdapter(Context context,ArrayList<HashMap<String, Object>> list,String num){
		this.mContext = context;
		this.mList = list;
		this.mNum = num;
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
	
	public void notifyList(ArrayList<HashMap<String, Object>> list){
		this.mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.my_indiana_info_item, null);
			mHolder = new ViewHolder();
			mHolder.mAuccountText = (TextView) convertView.findViewById(R.id.my_indiana_item_auccount);
			mHolder.mDateText = (TextView) convertView.findViewById(R.id.my_indiana_item_date);
			mHolder.mPhoneText = (TextView) convertView.findViewById(R.id.my_indiana_item_phone);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(mList.get(position).get("GameNum")!=null){
			if(mNum!=null && mList.get(position).get("GameNum").toString().equals(mNum)){
				mHolder.mAuccountText.setTextColor(mContext.getResources().getColor(R.color.red));
			}else{
				mHolder.mAuccountText.setTextColor(mContext.getResources().getColor(R.color.main_merchant_item_title_color));
			}
			mHolder.mAuccountText.setText(mList.get(position).get("GameNum").toString());
		}
		if(mList.get(position).get("CreateTimeFormat")!=null){
			mHolder.mDateText.setText(mList.get(position).get("CreateTimeFormat").toString());
		}
		if(mList.get(position).get("UserName")!=null && Constants.mUserName!=null){
			
			if(mList.get(position).get("UserName").toString().equals(Constants.mUserName)){
				mHolder.mPhoneText.setText(mList.get(position).get("UserName").toString());
			}else{
			mHolder.mPhoneText.setText(Utils.splitePhone(mList.get(position).get("UserName").toString()));
			}
		}
		return convertView;
	}
	class ViewHolder{
		private TextView mAuccountText;
		private TextView mDateText;
		private TextView mPhoneText;
	}
}
