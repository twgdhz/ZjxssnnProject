package com.zjxfood.adapter;

import android.content.Context;
import android.graphics.Typeface;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommodityListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private LayoutInflater mInflater;
	private BitmapUtils mBitmapUtils;
	private DecimalFormat df;
	private Double[] strs;

	public CommodityListAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(mContext);
		df = new DecimalFormat("0.00");
		mBitmapUtils = BitmapUtilSingle.getBitmapInstance(mContext);
		mBitmapUtils
				.configDefaultLoadFailedImage(R.drawable.merchant_occupying);
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

	public void notifyAddress(List<String> mList) {
		// this.mDistanceList = mList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.new_main_merchant_item, null);
			mHolder = new ViewHolder();

//			mHolder.mTuiText = (TextView) convertView
//					.findViewById(R.id.commodity_list_item_head_recommend);
			mHolder.mAddressText = (TextView) convertView
					.findViewById(R.id.commodity_item_address);
			mHolder.mMerchantName = (TextView) convertView
					.findViewById(R.id.commodity_list_item_head_name);
			mHolder.mLogImage = (ImageView) convertView
					.findViewById(R.id.commodity_item_image);
			mHolder.mRebateText = (TextView) convertView
					.findViewById(R.id.commodity_list_item_head_fanxian);
//			mHolder.mWifiText = (TextView) convertView
//					.findViewById(R.id.commodity_item_wifi_count);
			mHolder.mDistanceText = (TextView) convertView
					.findViewById(R.id.commodity_item_address_distance);
			mHolder.mMoneyText = (TextView) convertView
					.findViewById(R.id.commodity_item_per_capita_distance);
//			mHolder.mPlStarImage = (ImageView) convertView
//					.findViewById(R.id.commodity_item_comment_image);
			mHolder.msoldText = (TextView) convertView
					.findViewById(R.id.commodity_list_item_sold);
//			mHolder.mCurrencyText = (TextView) convertView
//					.findViewById(R.id.commodity_item_per_currency);

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
//				mHolder.mWifiText.setText("WIFI：有");
//			} else {
//				mHolder.mWifiText.setText("WIFI：无");
//			}
//		}
//		if (mList.get(position).get("istop") != null) {
//			if (mList.get(position).get("istop").toString().equals("true")) {
//				mHolder.mWifiText.setText("余额支付：支持");
//			} else {
//				mHolder.mWifiText.setText("余额支付：不支持");
//			}
//		}
		if (mList.get(position).get("ordercount") != null) {
			mHolder.msoldText.setText(mList.get(position).get("ordercount").toString());
		}
		if (mList.get(position).get("km") != null) {
			mHolder.mDistanceText.setText(mList.get(position).get("km") + "km");
		} else {
			if (mList.get(position).get("location") != null) {
				strs = Utils.getLalon(mList.get(position).get("location")
						.toString());
				if (strs != null && strs.length > 0) {
					if (strs[0] != null && strs[1] != null) {
						mHolder.mDistanceText.setText(df.format(Constants
								.getDistance(Constants.longt, Constants.lat,
										strs[0], strs[1]))
								+ "km");
					}
				}
			}
		}
		if (mList.get(position).get("money") != null) {
			mHolder.mMoneyText.setText( mList.get(position).get("money").toString());
		}
//		if (mList.get(position).get("iscurrency") != null) {
//			if (mList.get(position).get("iscurrency").toString().equals("0")) {
//				mHolder.mCurrencyText.setText("消费币：不支持");
//			} else if (mList.get(position).get("iscurrency").toString()
//					.equals("1")) {
//				mHolder.mCurrencyText.setText("消费币：支持");
//			}
//		}
//		Log.i("余额返利数据", mList.get(position).get("currencybackbl")+"===========");
//		if (mList.get(position).get("currencybackbl") != null) {
//
//			if (Float.parseFloat(mList.get(position).get("currencybackbl").toString())>0) {
//				mHolder.mCurrencyText.setText("余额返利："+mList.get(position).get("currencybackbl").toString()+"%");
//			} else {
////				mHolder.mCurrencyText.setText("消费币：支持");
//				mHolder.mCurrencyText.setVisibility(View.GONE);
//			}
//		}

//		if (mList.get(position).get("plstar") != null) {
//			if (mList.get(position).get("plstar").toString().equals("1")) {
//				mHolder.mPlStarImage
//						.setImageResource(R.drawable.evaluation_xingxing_png1);
//			} else if (mList.get(position).get("plstar").toString().equals("2")) {
//				mHolder.mPlStarImage
//						.setImageResource(R.drawable.evaluation_xingxing_png2);
//			} else if (mList.get(position).get("plstar").toString().equals("3")) {
//				mHolder.mPlStarImage
//						.setImageResource(R.drawable.evaluation_xingxing_png3);
//			} else if (mList.get(position).get("plstar").toString().equals("4")) {
//				mHolder.mPlStarImage
//						.setImageResource(R.drawable.evaluation_xingxing_png4);
//			} else if (mList.get(position).get("plstar").toString().equals("5")) {
//				mHolder.mPlStarImage
//						.setImageResource(R.drawable.evaluation_xingxing_png5);
//			} else if (mList.get(position).get("plstar").toString().equals("0")) {
//				mHolder.mPlStarImage
//						.setImageResource(R.drawable.evaluation_xingxing_png0);
//			}
//		}

		if (mList.get(position).get("Logoimage") != null) {
			mBitmapUtils.display(mHolder.mLogImage,
					mList.get(position).get("Logoimage").toString());
			mBitmapUtils.configDefaultBitmapMaxSize(100, 100);
		}

		Typeface typeface = Typeface.create("宋体", Typeface.NORMAL);
//		mHolder.mTuiText.setTypeface(typeface);
		mHolder.mAddressText.setTypeface(typeface);
		if (mList.get(position).get("Flnum") != null) {
			mHolder.mRebateText.setText("返利"
					+ (mList.get(position).get("Flnum").toString()).split("\\.")[0] + "%");
		}
		return convertView;
	}

	class ViewHolder {
		private TextView mMerchantName;
//		private TextView mTuiText;
		private TextView mAddressText;
		private ImageView mLogImage;
		private TextView mRebateText;
		private TextView mDistanceText;
//		private TextView mWifiText;
//		private ImageView mPlStarImage;
		private TextView mMoneyText;
		private TextView msoldText;
//		private TextView mCurrencyText;

	}

}
