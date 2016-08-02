package com.zjxfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.project.util.Utils;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;
import com.zjxfood.view.TimeListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DisplayAuctionAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private BitmapUtils mBitmapUtils;
	private int[] time;

	public DisplayAuctionAdapter(Context context,
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
		}
		if (!Constants.isNull(mList.get(position).get("ExtField1"))) {
			mHolder.mModel.setText(mList.get(position).get("ExtField1")
					.toString());
		}
		if (!Constants.isNull(mList.get(position).get("ExtField2"))) {
			mHolder.mScreen.setText(mList.get(position).get("ExtField2")
					.toString());
		}
		if (!Constants.isNull(mList.get(position).get("ExtField3"))) {
			mHolder.mMemory.setText(mList.get(position).get("ExtField3")
					.toString());
		}
		if (!Constants.isNull(mList.get(position).get("ExtField4")) && !mList.get(position).get("ExtField4").toString().equals("null")) {
			mHolder.mNumbers.setText(mList.get(position).get("ExtField4")
					.toString());
//			Log.i("ExtField4", mList.get(position).get("ExtField4")+"================");
		}else{
			mHolder.mNumbers.setText("");
		}
//		Log.i("mList2", mList.get(position).get("ExtField4")+"================");
		if (!Constants.isNull(mList.get(position).get("BasePrice"))) {
			mHolder.mStartingPrice.setText("起拍价："
					+ Float.parseFloat(mList.get(position).get("BasePrice")
							.toString()) + "食尚币");
		}
		if (!Constants.isNull(mList.get(position).get("ImgUrl"))) {
			mBitmapUtils.display(mHolder.mImageView,
					mList.get(position).get("ImgUrl").toString());
		}
		if (!Constants.isNull(mList.get(position).get("StartTime"))
				&& !Constants.isNull(mList.get(position).get("EndTime"))) {
			mHolder.mStartTime.setText(Utils.subTime(mList.get(position)
					.get("StartTime").toString())
					+ "开始");
			if (Utils.isStart(mList.get(position).get("StartTime").toString())) {
				time = formatStartTime(mList.get(position).get("StartTime")
						.toString());
				
				if (time != null) {
					mHolder.mTimedownview.setVisibility(View.VISIBLE);
					mHolder.mTimeShow.setVisibility(View.GONE);
//					Log.i("未开始", "==========未开始==========="+position);
//					Log.i("mTimedownview", time[0]+"==="+time[1]+"==="+time[2]+"==="+time[3]+"==="+position);
					mHolder.mTimedownview.setTimes(time);
					mHolder.mTimedownview.setType("未开始");
					mHolder.mTimedownview.setIsStart("nostart");
					if (!mHolder.mTimedownview.isRun()) {
						mHolder.mTimedownview.run();
					}
				}
			} else if (Utils.isEnd(mList.get(position).get("EndTime")
					.toString())) {
				mHolder.mTimedownview.setVisibility(View.VISIBLE);
				time = formatStartTime(mList.get(position).get("EndTime")
						.toString());
				if (time != null) {
					mHolder.mTimedownview.setVisibility(View.VISIBLE);
					mHolder.mTimeShow.setVisibility(View.GONE);
					mHolder.mTimedownview.setTimes(time);
					mHolder.mTimedownview.setType("竞拍中");
					mHolder.mTimedownview.setIsStart("isstart");
					if (!mHolder.mTimedownview.isRun()) {
						mHolder.mTimedownview.run();
					}
				}
			} else {
				mHolder.mTimedownview.setType("已结束");
				mHolder.mTimedownview.setVisibility(View.GONE);
				mHolder.mTimeShow.setText("已结束");
				mHolder.mTimeShow.setVisibility(View.VISIBLE);
//				mHolder.mTimedownview.cancel();
//				mHolder.mTimedownview.end();
			}
		}
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

	private int[] formatStartTime(String startTime) {
		int[] times = { 0, 0, 0, 0 };
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long time = 0;
		try {
			Date newStartTime = df.parse(startTime);
			Date newEndTime = new Date();
			time = (newStartTime.getTime() - newEndTime.getTime());
		} catch (Exception e) {
		}
		if (time > 0) {
			int day = (int) (time / (24 * 60 * 60 * 1000));
			int hour = (int) (time / (60 * 60 * 1000) - day * 24);
			int min = (int) ((time / (60 * 1000)) - day * 24 * 60 - hour * 60);
			int senc = (int) (time / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
			times[0] = 0;
			times[1] = hour;
			times[2] = min;
			times[3] = senc;
			return times;
		} else {
			return times;
		}
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
