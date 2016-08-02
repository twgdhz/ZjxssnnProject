package com.zjxfood.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.zjxfood.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LogisticsListAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	
	public LogisticsListAdapter(Context context,ArrayList<HashMap<String, Object>> list){
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if(convertView==null){
			mHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.logistics_list_item, null);
			mHolder.mTextView = (TextView) convertView.findViewById(R.id.logistics_item_content_text);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(position>0 && position<mList.size()-1){
		mHolder.mTextView.setText(mList.get(position).get("context")+"\n"+mList.get(position).get("time"));
		}
		return convertView;
		
	}
	class ViewHolder{
		TextView mTextView;
	}
}
