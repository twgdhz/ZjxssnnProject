package com.zjxfood.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjxfood.activity.R;

public class RetrieveSpinnerAdapter extends BaseAdapter{

	private Context mContext;
	private String[] mStrs;
	public RetrieveSpinnerAdapter(Context context,String[] strs){
		this.mContext = context;
		this.mStrs = strs;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mStrs.length;
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
			convertView = inflater.inflate(R.layout.popup_retrieve_spinner_list_item, null);
			mHolder = new ViewHolder();
			mHolder.mTextView = (TextView) convertView.findViewById(R.id.popup_retrieve_item_text);
			
			convertView.setTag(mHolder);
			
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		mHolder.mTextView.setText(mStrs[position]);
		return convertView;
	}

	class ViewHolder{
		TextView mTextView;
	}
}
