package com.zjxfood.cashindiana;

import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.project.util.ScreenUtils;
import com.zjxfood.activity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CashIndianaListAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<HashMap<String, Object>> mList;
	private BitmapUtils mBitmapUtils;
	private float num;
	
	public CashIndianaListAdapter(Context context,ArrayList<HashMap<String, Object>> list){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mBitmapUtils = BitmapUtilSingle.getBitmapInstance(mContext);
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
	
	public void notifyList(ArrayList<HashMap<String, Object>> list){
		this.mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.cash_list_item_layout, null);
			mHolder = new ViewHolder();
			mHolder.mBar = (ProgressBar) convertView.findViewById(R.id.indiana_people_bar);
			mHolder.mImageView = (ImageView) convertView.findViewById(R.id.indiana_item_image);
			mHolder.mName = (TextView) convertView.findViewById(R.id.indiana_item_number_text);
			mHolder.mNumLeast = (TextView) convertView.findViewById(R.id.indiana_item_people_text);
			mHolder.mTotalPerson = (TextView) convertView.findViewById(R.id.indiana_item_people_number_text);
			LayoutParams params = mHolder.mImageView.getLayoutParams();
			params.height = (int) (ScreenUtils.getScreenWidth(mContext)*(0.75));
			mHolder.mImageView.setLayoutParams(params);
//			mHolder.mStartText = (TextView) convertView.findViewById(R.id.indiana_item_number_text_start);
//			mHolder.mEndText = (TextView) convertView.findViewById(R.id.indiana_item_number_text_end);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		if(null!=mList.get(position).get("ProductName") && mList.get(position).get("Id")!=null){
			mHolder.mName.setText("【第"+mList.get(position).get("Id")+"期】"+mList.get(position).get("ProductName").toString());
		}
		if(null!=mList.get(position).get("NumLeast")){
			mHolder.mNumLeast.setText("总需"+mList.get(position).get("NumLeast").toString()+"人次");
		}
		if(null!=mList.get(position).get("TotalPerson")){
			mHolder.mTotalPerson.setText("已达"+mList.get(position).get("TotalPerson").toString()+"人次");
		}
		if(null!=mList.get(position).get("ImgUrl")){
			mBitmapUtils.display(mHolder.mImageView, mList.get(position).get("ImgUrl").toString());
		}
//		if(null!=mList.get(position).get("StartTime")){
//			mHolder.mStartText.setText(mList.get(position).get("StartTime").toString()+"\t开始");
//		}
//		if(null!=mList.get(position).get("EndTime")){
//			mHolder.mEndText.setText(mList.get(position).get("EndTime").toString()+"\t结束");
//		}
		if(null!=mList.get(position).get("NumLeast") && null!=mList.get(position).get("TotalPerson")){
			num = Float.parseFloat(mList.get(position).get("TotalPerson").toString())/Float.parseFloat(mList.get(position).get("NumLeast").toString());
			mHolder.mBar.setProgress((int) (num*100));
		}
		
		return convertView;
	}
	class ViewHolder{
		private ProgressBar mBar;
		private ImageView mImageView;
		private TextView mName;
		private TextView mNumLeast;
		private TextView mTotalPerson;
		private TextView mStartText;
		private TextView mEndText;
	}
}
