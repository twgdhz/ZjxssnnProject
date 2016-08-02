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

public class PopMallTwoTypeAdapter extends BaseAdapter{

	private Context mContext;
	private String[] mArrays;
	private int choseNum=-1;
	private ArrayList<HashMap<String, Object>> mList;
	
	public PopMallTwoTypeAdapter(Context context,ArrayList<HashMap<String, Object>> list){
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
	public void choseItem(int position){
		choseNum = position;
		notifyDataSetChanged();
	}
//	public void nofityList(){
//		
//	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.popupwindow_mall_list_type_item2, null);
			mHolder = new ViewHolder();
			mHolder.mTextView = (TextView) convertView.findViewById(R.id.pop_comm_type_list_name);
			mHolder.mLayout = (LinearLayout) convertView.findViewById(R.id.popup_mall_list_type_layout);
			mHolder.mView = convertView.findViewById(R.id.popup_mall_list_two_type_view);
			convertView.setTag(mHolder);
			
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(choseNum==position){
			mHolder.mLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
			mHolder.mView.setVisibility(View.VISIBLE);
		}else{
			mHolder.mLayout.setBackgroundColor(mContext.getResources().getColor(R.color.mall_popup_chose_color1));
			mHolder.mView.setVisibility(View.GONE);
		}
		
		mHolder.mTextView.setText(mList.get(position).get("name").toString());
		return convertView;
	}
	
	class ViewHolder{
		TextView mTextView;
		LinearLayout mLayout;
		View mView;
	}

}
