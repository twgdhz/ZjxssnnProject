package com.zjxfood.adapter;



import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjxfood.activity.R;

public class PopMallThreeTypeAdapter extends BaseAdapter{

	private Context mContext;
	private String[] mArrays;
	private int choseNum=-1;
	private ArrayList<HashMap<String, Object>> mList;
	public PopMallThreeTypeAdapter(Context context,ArrayList<HashMap<String, Object>> list){
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
//	public void choseItem(int position){
//		choseNum = position;
//		notifyDataSetChanged();
//	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.popupwindow_commodity_list_type_item, null);
			mHolder = new ViewHolder();
			mHolder.mTextView = (TextView) convertView.findViewById(R.id.pop_comm_type_list_name);
		
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
