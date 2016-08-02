package com.zjxfood.activity;

import java.util.ArrayList;
import java.util.HashMap;
import com.zjxfood.common.Constants;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AuctionRecordAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private LayoutInflater mInflater;

	public AuctionRecordAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(mContext);
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
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.auction_record_list_item,
					null);
			mHolder = new ViewHolder();
			mHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.auction_record_item_image);
			mHolder.mPhoneText = (TextView) convertView
					.findViewById(R.id.auction_record_item_username);
			mHolder.mPriceText = (TextView) convertView
					.findViewById(R.id.auction_record_item_price);
			mHolder.mDateText = (TextView) convertView
					.findViewById(R.id.auction_record_item_date);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if (!Constants.isNull(mList.get(position).get("UserName"))) {
			if (Constants.mUserName != null
					&& Constants.mUserName.equals(mList.get(position)
							.get("UserName").toString())) {
				mHolder.mPhoneText.setText(mList.get(position).get("UserName")
						.toString());
			} else {
				mHolder.mPhoneText.setText(splitePhone(mList.get(position)
						.get("UserName").toString()));
			}
		}
		if (!Constants.isNull(mList.get(position).get("Price"))) {
			mHolder.mPriceText.setText(Float.parseFloat(mList.get(position)
					.get("Price").toString())
					+ "币");
		}
		if (!Constants.isNull(mList.get(position).get("CreateTime"))) {
//			SimpleDateFormat df=new SimpleDateFormat("MM-dd HH:mm");
//			Date date = null;
//				date = df.parse(mList.get(position).get("CreateTime").toString());
				mHolder.mDateText.setText(mList.get(position).get("CreateTime").toString());
			
			
		}
		if (position == 0) {
			mHolder.mImageView.setImageResource(R.drawable.lingxian);
		}else{
			mHolder.mImageView.setImageResource(R.drawable.luohou);
		}
		return convertView;
	}

	class ViewHolder {
		private ImageView mImageView;
		private TextView mPhoneText;
		private TextView mPriceText;
		private TextView mDateText;
	}

	public static String splitePhone(String phone) {
		String[] tel = new String[phone.length()];
		StringBuffer sb = new StringBuffer();
		if (tel.length > 0) {
			for (int i = 0; i < tel.length; i++) {
				if (i > 2 && i < 7) {
					sb.append("*");
				} else {
					sb.append(phone.charAt(i));
				}
			}
		}
		return sb.toString();
	}

	private String getTime(String time) {
		String newTime = "";
		newTime = time.substring(time.length() - 14, time.length() - 3);
		return newTime;
	}
}
