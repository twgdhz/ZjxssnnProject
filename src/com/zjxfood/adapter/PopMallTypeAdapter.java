package com.zjxfood.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjxfood.activity.R;

public class PopMallTypeAdapter extends BaseAdapter{

	private Context mContext;
	private String[] mArrays;
	private int choseNum=-1;
	
	public PopMallTypeAdapter(Context context,String[] arrays){
		this.mContext = context;
		this.mArrays = arrays;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrays.length;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.popupwindow_mall_one_list_type_item, null);
			mHolder = new ViewHolder();
			mHolder.mTextView = (TextView) convertView.findViewById(R.id.pop_comm_type_list_name);
			mHolder.mLayout = (LinearLayout) convertView.findViewById(R.id.popup_mall_list_type_layout);
			mHolder.mImageView = (ImageView) convertView.findViewById(R.id.pop_comm_type_list_image);
			mHolder.mView = convertView.findViewById(R.id.popup_mall_list_type_view);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(choseNum==position){
			mHolder.mLayout.setBackgroundColor(mContext.getResources().getColor(R.color.mall_popup_chose_color1));
//			mHolder.mImageView.setVisibility(View.VISIBLE);
			mHolder.mView.setVisibility(View.VISIBLE);
		}else{
			mHolder.mLayout.setBackgroundColor(mContext.getResources().getColor(R.color.mall_popup_color1));
//			mHolder.mImageView.setVisibility(View.GONE);
			mHolder.mView.setVisibility(View.GONE);
		}
		
		mHolder.mTextView.setText(mArrays[position]);
		return convertView;
	}
	
	class ViewHolder{
		TextView mTextView;
		ImageView mImageView;
		LinearLayout mLayout;
		View mView;
	}

}
