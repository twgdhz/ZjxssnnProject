package com.zjxfood.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjxfood.activity.R;

public class GoldAdapter extends BaseAdapter{

	private Context mContext;
	private String[] mArrays;
	public GoldAdapter(Context context, String[] arrays){
		this.mContext = context;
		this.mArrays = arrays;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrays.length;
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
			convertView = inflater.inflate(R.layout.gold_list_type_item, null);
			mHolder = new ViewHolder();
			mHolder.mTextView = (TextView) convertView.findViewById(R.id.pop_comm_type_list_name);
			
			convertView.setTag(mHolder);
			
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.mTextView.setText(mArrays[position]);
		return convertView;
	}
	
	class ViewHolder{
		TextView mTextView;
	}

}
