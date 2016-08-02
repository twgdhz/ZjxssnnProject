package com.zjxfood.adapter;



import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.zjxfood.activity.R;

public class AddressManListAdapter extends BaseAdapter{

	private Context mContext;
	private float mWidth;
	public AddressManListAdapter(Context context,float width){
		this.mContext = context;
		this.mWidth = width;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.address_management_list_item, null);
			mHolder = new ViewHolder();
			
			mHolder.mContentLayout = (RelativeLayout) convertView.findViewById(R.id.address_management_content_layouts);
			mHolder.mDeleteLayout = (RelativeLayout) convertView.findViewById(R.id.address_manage_delete_layout);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.mDeleteLayout.setX(mWidth);
		return convertView;
	}
	
	class ViewHolder{
		private RelativeLayout mDeleteLayout;
		private RelativeLayout mContentLayout;
	}

}
