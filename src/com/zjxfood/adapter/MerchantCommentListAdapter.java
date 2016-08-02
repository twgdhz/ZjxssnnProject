package com.zjxfood.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zjxfood.activity.R;

public class MerchantCommentListAdapter extends BaseAdapter{

	private Context mContext;
	public MerchantCommentListAdapter(Context context){
		this.mContext = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
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
		ViewHolder mHolder;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.merchat_comment_list_item, null);
			mHolder = new ViewHolder();
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	class ViewHolder{
		
	}
}
