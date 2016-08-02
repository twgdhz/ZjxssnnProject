package com.zjxfood.indiana;

import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.project.util.Utils;
import com.zjxfood.activity.R;
import com.zjxfood.indiana.MyIndianaOrderAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyIndianaNoPayAdapter extends BaseAdapter{

	private Context mContext;
	private BitmapUtils mBitmapUtils;
	private LayoutInflater mInflater;
	private ArrayList<HashMap<String, Object>> mList;
	public MyIndianaNoPayAdapter(Context context,ArrayList<HashMap<String, Object>> list){
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(mContext);
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.my_indiana_no_pay_item, null);
			mHolder = new ViewHolder();
			mHolder.mImageView = (ImageView) convertView.findViewById(R.id.my_indiana_no_pay_item_image);
			mHolder.mNameText = (TextView) convertView.findViewById(R.id.my_indiana_no_pay_item_content_name);
			mHolder.mOrderText = (TextView) convertView.findViewById(R.id.my_indiana_no_pay_item_order);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(mList.get(position).get("ImgUrl")!=null){
			mBitmapUtils.display(mHolder.mImageView, mList.get(position).get("ImgUrl").toString());
		}
		if(mList.get(position).get("ProductName")!=null && mList.get(position).get("Id")!=null){
			mHolder.mNameText.setText("【"+mList.get(position).get("Id")+"期】"+mList.get(position).get("ProductName"));
		}
		if(mList.get(position).get("OrderId")!=null){
			mHolder.mOrderText.setText("订单号："+mList.get(position).get("OrderId").toString());
		}
		return convertView;
	}
	class ViewHolder{
		private ImageView mImageView;
		private TextView mNameText;
		private TextView mOrderText;
	}
}
