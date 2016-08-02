package com.zjxfood.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.project.util.Utils;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;
import com.zjxfood.view.TimeDownView;
import com.zjxfood.view.TimeListView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayAuctionTemporaryAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private BitmapUtils mBitmapUtils;
	private int[] time;

	public DisplayAuctionTemporaryAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mList = list;
		mBitmapUtils = BitmapUtilSingle.getBitmapInstance(mContext);
		mBitmapUtils
				.configDefaultLoadFailedImage(R.drawable.main_paimai_zhanwei);
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

	public void notifyList(ArrayList<HashMap<String, Object>> list) {
		this.mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.display_auction_list_item,
					null);
			mHolder = new ViewHolder();
			mHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.display_auction_item_content_image);
			mHolder.mName = (TextView) convertView
					.findViewById(R.id.main_timel_limit_auction_content_name);
			mHolder.mModel = (TextView) convertView
					.findViewById(R.id.main_timel_limit_auction_content_model);
			mHolder.mScreen = (TextView) convertView
					.findViewById(R.id.main_timel_limit_auction_content_screen);
			mHolder.mMemory = (TextView) convertView
					.findViewById(R.id.main_timel_limit_auction_content_memory);
			mHolder.mNumbers = (TextView) convertView
					.findViewById(R.id.main_timel_limit_auction_content_numbers);
			mHolder.mStartingPrice = (TextView) convertView
					.findViewById(R.id.main_timel_limit_auction_content_price);
			mHolder.mStartTime = (TextView) convertView
					.findViewById(R.id.display_auction_item_head_time);
			mHolder.mTimedownview = (TimeListView) convertView
					.findViewById(R.id.display_auction_item_timedownview);
			mHolder.mTimeShow = (TextView) convertView
					.findViewById(R.id.display_auction_item_head_haisheng);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if (!Constants.isNull(mList.get(position).get("ProductName"))) {
			mHolder.mName.setText(mList.get(position).get("ProductName")
					.toString());
		}else{
			mHolder.mName.setText("");
		}
		if (!Constants.isNull(mList.get(position).get("ExtField1"))) {
			mHolder.mModel.setText(mList.get(position).get("ExtField1")
					.toString());
		}else{
			mHolder.mModel.setText("");
		}
		if (!Constants.isNull(mList.get(position).get("ExtField2"))) {
			mHolder.mScreen.setText(mList.get(position).get("ExtField2")
					.toString());
		}else{
			mHolder.mScreen.setText("");
		}
		if (!Constants.isNull(mList.get(position).get("ExtField3"))) {
			mHolder.mMemory.setText(mList.get(position).get("ExtField3")
					.toString());
		}else{
			mHolder.mMemory.setText("");
		}
		if (!Constants.isNull(mList.get(position).get("ExtField4"))) {
			mHolder.mNumbers.setText(mList.get(position).get("ExtField4")
					.toString());
		}else{
			mHolder.mNumbers.setText("");
		}
		if (!Constants.isNull(mList.get(position).get("BasePrice"))) {
			mHolder.mStartingPrice.setText("起拍价："
					+ Float.parseFloat(mList.get(position).get("BasePrice")
							.toString()) + "食尚币");
		}
		if (!Constants.isNull(mList.get(position).get("ImgUrl"))) {
			mBitmapUtils.display(mHolder.mImageView,
					mList.get(position).get("ImgUrl").toString());
		}
		if (!Constants.isNull(mList.get(position).get("StartTime"))) {
			mHolder.mStartTime.setText(mList.get(position).get("StartTime").toString()+"开始");
		}
		mHolder.mTimedownview.setVisibility(View.GONE);
		mHolder.mTimeShow.setVisibility(View.VISIBLE);
//		mHolder.mTimeShow.setText("已结束");
		return convertView;
	}
	
	class ViewHolder {
		private ImageView mImageView;
		private TextView mName;
		private TextView mModel;
		private TextView mScreen;
		private TextView mMemory;
		private TextView mNumbers;
		private TextView mStartingPrice;
		private TextView mStartTime;
		private TimeListView mTimedownview;
		private TextView mTimeShow;
	}

	// 判断是否开始
	// private boolean isStart(String startTime) {
	// DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// long time = 0;
	// try{
	// Date newStartTime = df.parse(startTime);
	// Date newEndTime = new Date();
	// time = (newStartTime.getTime()-newEndTime.getTime());
	// }catch(Exception e){}
	// if(time>0){
	// return true;
	// }else{
	// return false;
	// }
	// }
	// //判断是否结束
	// private boolean isEnd(String endTime) {
	// DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// long time = 0;
	// try{
	// Date newEndTime = df.parse(endTime);
	// Date currentTime = new Date();
	// time = (newEndTime.getTime()-currentTime.getTime());
	// }catch(Exception e){}
	// if(time>0){
	// return true;
	// }else{
	// return false;
	// }
	// }
}
