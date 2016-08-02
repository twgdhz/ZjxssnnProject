package com.zjxfood.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.zjxfood.activity.R;
import com.zjxfood.adapter.AddressManListAdapter.ViewHolder;
import com.zjxfood.delete.list.OnDeleteListioner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewAddressManageListAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private boolean delete = false;
	private OnDeleteListioner mOnDeleteListioner;
	
	public NewAddressManageListAdapter(Context context,ArrayList<HashMap<String, Object>> list){
		this.mContext = context;
		this.mList = list;
	}
	
	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setOnDeleteListioner(OnDeleteListioner mOnDeleteListioner) {
		this.mOnDeleteListioner = mOnDeleteListioner;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.new_address_management_list_item, null);
			mHolder = new ViewHolder();
			mHolder.mUserName = (TextView) convertView.findViewById(R.id.address_manage_item_user_name);
			mHolder.mDelete = (TextView) convertView.findViewById(R.id.new_address_manage_delete_action);
			mHolder.mPhone = (TextView) convertView.findViewById(R.id.address_manage_item_user_phone);
			mHolder.mAddress = (TextView) convertView.findViewById(R.id.address_manage_detail_text);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.mUserName.setText(mList.get(position).get("Realname").toString());
		
		mHolder.mPhone.setText(mList.get(position).get("Mobile").toString());
		if(mList.get(position).get("Provincename")!=null && mList.get(position).get("Cityname")!=null && mList.get(position).get("Areaname")!=null)
		mHolder.mAddress.setText(mList.get(position).get("Provincename").toString()+"\t"+mList.get(position).get("Cityname").toString()+"\t"+mList.get(position).get("Areaname").toString()+"："+mList.get(position).get("Address").toString());
		
		final OnClickListener mOnClickListener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mOnDeleteListioner != null)
					mOnDeleteListioner.onDelete(position);
			}
		};
		mHolder.mDelete.setOnClickListener(mOnClickListener);
		mHolder.mUserName.setText(mList.get(position).get("Realname").toString());
		return convertView;
	}

	class ViewHolder{
		private TextView mUserName;
		private TextView mPhone;
		private TextView mAddress;
		private TextView mDelete;
	}
}
