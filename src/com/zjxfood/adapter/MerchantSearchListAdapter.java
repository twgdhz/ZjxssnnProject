package com.zjxfood.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.SystemUtils;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;

public class MerchantSearchListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
//	private List<String> mDistanceList;
	private BitmapUtils mBitmapUtils;
	private Double[] strs;
	private DecimalFormat df;

	public MerchantSearchListAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mList = list;
		mBitmapUtils = new BitmapUtils(mContext);
		df = new DecimalFormat("###.00"); 
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

	public void notifyList(ArrayList<HashMap<String, Object>> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

//	public void notifyAddress(List<String> mList) {
//		this.mDistanceList = mList;
//	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.commodity_list_item, null);
			mHolder = new ViewHolder();

			mHolder.mTuiText = (TextView) convertView
					.findViewById(R.id.commodity_list_item_head_recommend);
			// mHolder.mCharacteristicText = (TextView) convertView
			// .findViewById(R.id.commodity_item_per_capita_characteristic);
			// mHolder.mConsumptionText = (TextView) convertView
			// .findViewById(R.id.commodity_item_consumption_count);
			mHolder.mAddressText = (TextView) convertView
					.findViewById(R.id.commodity_item_address);
			mHolder.mMerchantName = (TextView) convertView
					.findViewById(R.id.commodity_list_item_head_name);
			mHolder.mLogImage = (ImageView) convertView
					.findViewById(R.id.commodity_item_image);
			mHolder.mRebateText = (TextView) convertView
					.findViewById(R.id.commodity_list_item_head_fanxian);
			mHolder.mWifiText = (TextView) convertView
					.findViewById(R.id.commodity_item_wifi_count);
			mHolder.mDistanceText = (TextView) convertView
					.findViewById(R.id.commodity_item_address_distance);
			mHolder.mPlStarImage = (ImageView) convertView
					.findViewById(R.id.commodity_item_comment_image);
			mHolder.mOldContent = (TextView) convertView
					.findViewById(R.id.commodity_list_item_sold);
			mHolder.mMoneyText = (TextView) convertView
					.findViewById(R.id.commodity_item_per_capita_distance);
			mHolder.mCurrencyText = (TextView) convertView.findViewById(R.id.commodity_item_per_currency);
			convertView.setTag(mHolder);

		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if (mList.get(position).get("Merchantname") != null) {
			mHolder.mMerchantName.setText(mList.get(position)
					.get("Merchantname").toString());
		}
		if (mList.get(position).get("Address") != null) {
			mHolder.mAddressText.setText(mList.get(position).get("Address")
					.toString());
		}
//		if (mList.get(position).get("Iswifi") != null) {
//			if (mList.get(position).get("Iswifi").toString().equals("1")) {
//				mHolder.mWifiText.setText("WIFI：开");
//			} else {
//				mHolder.mWifiText.setText("WIFI：关");
//			}
//		}
		if (mList.get(position).get("istop") != null) {
			if (mList.get(position).get("istop").toString().equals("true")) {
				mHolder.mWifiText.setText("余额支付：支持");
			} else {
				mHolder.mWifiText.setText("余额支付：不支持");
			}
		}
//		if (mList.get(position).get("iscurrency") != null) {
//			if(mList.get(position).get("iscurrency").toString().equals("0")){
//				mHolder.mCurrencyText.setText("消费币：不支持");
//			}else if(mList.get(position).get("iscurrency").toString().equals("1")){
//				mHolder.mCurrencyText.setText("消费币：支持");
//			}
//		}
		
//		if (mDistanceList != null) {
//			mHolder.mDistanceText.setText(mDistanceList.get(position));
//		}
		if (mList.get(position).get("location") != null) {
			strs = getLalon(mList.get(position).get("location").toString());
			if (strs != null && strs.length > 0) {
				if (strs[0] != null && strs[1] != null) {
					mHolder.mDistanceText.setText(df.format(Constants.getDistance(
							Constants.longt, Constants.lat, strs[0], strs[1]))
							+ "km");
				}
			}
		}
		if (mList.get(position).get("ordercount") != null) {
			mHolder.mOldContent.setText("已售："+mList.get(position).get("ordercount")
					.toString());
		}
		if (SystemUtils.getSystemVersion() >= SystemUtils.V4_0) {
			mHolder.mLogImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		if(mList.get(position).get("Logoimage")!=null){
			mBitmapUtils.display(mHolder.mLogImage,
					mList.get(position).get("Logoimage").toString());
		}
		Typeface typeface = Typeface.create("宋体", Typeface.NORMAL);
		mHolder.mTuiText.setTypeface(typeface);
		mHolder.mAddressText.setTypeface(typeface);
		if (mList.get(position).get("Flnum") != null) {
			mHolder.mRebateText.setText("返现"
					+ mList.get(position).get("Flnum").toString() + "%");
		}
		if (mList.get(position).get("money") != null) {
			mHolder.mMoneyText.setText("人均￥"
					+ mList.get(position).get("money").toString());
		}
		if (mList.get(position).get("plstar") != null) {
			if (mList.get(position).get("plstar").toString().equals("1")) {
				mHolder.mPlStarImage
						.setImageResource(R.drawable.evaluation_xingxing_png1);
			} else if (mList.get(position).get("plstar").toString().equals("2")) {
				mHolder.mPlStarImage
						.setImageResource(R.drawable.evaluation_xingxing_png2);
			} else if (mList.get(position).get("plstar").toString().equals("3")) {
				mHolder.mPlStarImage
						.setImageResource(R.drawable.evaluation_xingxing_png3);
			} else if (mList.get(position).get("plstar").toString().equals("4")) {
				mHolder.mPlStarImage
						.setImageResource(R.drawable.evaluation_xingxing_png4);
			} else if (mList.get(position).get("plstar").toString().equals("5")) {
				mHolder.mPlStarImage
						.setImageResource(R.drawable.evaluation_xingxing_png5);
			}
			else if (mList.get(position).get("plstar").toString().equals("0")) {
				mHolder.mPlStarImage
						.setImageResource(R.drawable.evaluation_xingxing_png0);
			}
		} else {
			mHolder.mPlStarImage
					.setImageResource(R.drawable.evaluation_xingxing_png0);
		}
		return convertView;
	}

	class ViewHolder {
		private TextView mMerchantName;
		private TextView mTuiText;
		private TextView mAddressText;
		private ImageView mLogImage;
		private TextView mRebateText;
		private TextView mDistanceText;
		private TextView mWifiText;
		private ImageView mPlStarImage;
		private TextView mMoneyText;
		private TextView mOldContent;
		private TextView mCurrencyText;
	}
	private Double[] getLalon(String str) {
		Double[] strs = new Double[2];
		int a = str.indexOf(",");
		if (a > 0 && a < str.length()) {
			strs[0] = Double.parseDouble(str.substring(0, a - 1));
			strs[1] = Double
					.parseDouble(str.substring(a + 1, str.length() - 1));
		}
		return strs;
	}
}
