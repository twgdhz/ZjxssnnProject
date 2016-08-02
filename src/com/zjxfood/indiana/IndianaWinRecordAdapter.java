package com.zjxfood.indiana;

import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.project.util.Utils;
import com.tencent.open.utils.Util;
import com.zjxfood.activity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IndianaWinRecordAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<HashMap<String, Object>> mList;
	private BitmapUtils mBitmapUtils;
	public IndianaWinRecordAdapter(Context context,ArrayList<HashMap<String, Object>> list){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		this.mList = list;
		mBitmapUtils = BitmapUtilSingle.getBitmapInstance(mContext);
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
	public void notifyList(ArrayList<HashMap<String, Object>> list){
		this.mList.addAll(list);
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.indiana_win_record_list_item, null);
			mHolder = new ViewHolder();
			convertView.setTag(mHolder);
			
			mHolder.mImageView = (ImageView) convertView.findViewById(R.id.indiana_record_item_image);
			mHolder.mMallText = (TextView) convertView.findViewById(R.id.indiana_record_item_name);
			mHolder.mUserName = (TextView) convertView.findViewById(R.id.indiana_record_item_user);
			mHolder.mDateText = (TextView) convertView.findViewById(R.id.indiana_record_item_date);
			
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(mList.get(position).get("ImgUrl")!=null){
			mBitmapUtils.display(mHolder.mImageView, mList.get(position).get("ImgUrl").toString());
		}
		if(mList.get(position).get("ProductName")!=null){
			mHolder.mMallText.setText(mList.get(position).get("ProductName").toString());
		}
		if(mList.get(position).get("LuckyNum")!=null){
			
			mHolder.mUserName.setText("中奖号："+mList.get(position).get("LuckyNum").toString());
		}
		if(mList.get(position).get("CreateTime")!=null){
			mHolder.mDateText.setText("时间："+mList.get(position).get("CreateTime").toString());
		}
		return convertView;
	}
	class ViewHolder{
		private ImageView mImageView;
		private TextView mMallText;
		private TextView mUserName;
		private TextView mDateText;
		
	}
}
