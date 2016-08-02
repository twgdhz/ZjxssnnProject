package com.zjxfood.red;

import java.util.ArrayList;
import java.util.HashMap;

import com.zjxfood.activity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RedIssuedListAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	
	public RedIssuedListAdapter(Context context,ArrayList<HashMap<String, Object>> list){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		this.mList = list;
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
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
			convertView = mInflater.inflate(R.layout.red_issued_list_item, null);
			mHolder = new ViewHolder();
			mHolder.mNameText = (TextView) convertView.findViewById(R.id.red_issued_top_text1);
			mHolder.mPriceText = (TextView) convertView.findViewById(R.id.red_issued_top_text2);
			mHolder.mDateText = (TextView) convertView.findViewById(R.id.red_issued_bottom_text1);
			mHolder.mNumberText = (TextView) convertView.findViewById(R.id.red_issued_bottom_text2);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(mList.get(position).get("Memo")!=null){
			mHolder.mNameText.setText(mList.get(position).get("Memo").toString());
		}
		if(mList.get(position).get("HbMoney")!=null){
			mHolder.mPriceText.setText(mList.get(position).get("HbMoney").toString()+"元");
		}
		if(mList.get(position).get("CreateOn")!=null){
			mHolder.mDateText.setText(mList.get(position).get("CreateOn").toString());
		}
		if(mList.get(position).get("TotalNum")!=null && mList.get(position).get("RemainNum")!=null){
			int a = Integer.parseInt(mList.get(position).get("TotalNum").toString())-Integer.parseInt(mList.get(position).get("RemainNum").toString());
			mHolder.mNumberText.setText(a+"/"+mList.get(position).get("TotalNum"));
		}
		return convertView;
	}
	class ViewHolder{
		private TextView mNameText;
		private ImageView mHeadImage;
		private TextView mPriceText;
		private TextView mDateText;
		private TextView mNumberText;
	}
}
