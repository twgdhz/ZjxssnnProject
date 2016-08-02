package com.zjxfood.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.zjxfood.activity.R;
import com.zjxfood.adapter.AddressManListAdapter.ViewHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MallParameterAdapter extends BaseAdapter{

	private Context mContext;
	private HashMap<String, Object> mMap;
	private ArrayList<HashMap<String, Object>> mList;
	public MallParameterAdapter(Context context,ArrayList<HashMap<String, Object>> list){
		this.mContext = context;
		this.mList = list;
		Log.i("adapter", mList+"==============");
//		Set set = map.entrySet();
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
			convertView = inflater.inflate(R.layout.mall_parameter_list_item_layout, null);
			mHolder = new ViewHolder();
			mHolder.mNameText = (TextView) convertView.findViewById(R.id.mall_parameter_item_name);
			mHolder.mValueText = (TextView) convertView.findViewById(R.id.mall_parameter_item_value);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
//		Log.i("adapter", mList.get(position).get("name").toString()+"==============");
		if(mList.get(position).get("name")!=null){
		mHolder.mNameText.setText(mList.get(position).get("name").toString());
		}
		if(mList.get(position).get("value")!=null){
		mHolder.mValueText.setText(mList.get(position).get("value").toString());
		}
//		mHolder.mNameText.setText("衣服");
//		mHolder.mValueText.setText("衣服");
		
		return convertView;
	}
	class ViewHolder{
		TextView mNameText;
		TextView mValueText;
	}
}
