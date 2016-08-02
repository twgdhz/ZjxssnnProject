package com.zjxfood.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.xutils.BitmapUtils;
import com.zjxfood.activity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MainImageAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String,Object>> mArrayList;
	private BitmapUtils mBitmapUtils;
	
	public MainImageAdapter(Context context,ArrayList<HashMap<String, Object>> list){
		this.mContext = context;
		this.mArrayList = list;
		mBitmapUtils = new BitmapUtils(mContext);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayList.size();
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
			convertView = inflater.inflate(R.layout.main_image_list_item, null);
			mHolder = new ViewHolder();
			
			mHolder.mImageView = (ImageView) convertView.findViewById(R.id.main_image_item);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		try {
//			ImageLoader.getInstance().displayImage(
//					mArrayList.get(position).get("Logoimage").toString(),
//					mHolder.mImageView, options);
			mBitmapUtils.display(mHolder.mImageView, mArrayList.get(position).get("Logoimage").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	
	class ViewHolder{
		private ImageView mImageView;
	}
}
