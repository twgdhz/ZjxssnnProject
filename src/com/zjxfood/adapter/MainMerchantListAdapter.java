package com.zjxfood.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.project.util.SystemUtils;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMerchantListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private BitmapUtils mBitmapUtils;
	private Double[] strs;
	private DecimalFormat df;
	public MainMerchantListAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mList = list;
		df = new DecimalFormat("###.00"); 
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.merchant_list_item, null);
			mHolder = new ViewHolder();

			mHolder.mMerchantName = (TextView) convertView
					.findViewById(R.id.merchant_list_item_head_name);
			mHolder.mWifiText = (TextView) convertView
					.findViewById(R.id.merchant_item_wifi_count);

			mHolder.mAddressText = (TextView) convertView
					.findViewById(R.id.merchant_item_address);
			mHolder.mPlStarImage = (ImageView) convertView
					.findViewById(R.id.merchant_item_comment_image);
			mHolder.mMerchantImage = (ImageView) convertView
					.findViewById(R.id.merchant_item_image);
			mHolder.mMoneyText = (TextView) convertView
					.findViewById(R.id.merchant_item_per_capita_distance);
			mHolder.mFlumText = (TextView) convertView
					.findViewById(R.id.main_merchant_list_item_fanxian);
			mHolder.mDistanceText = (TextView) convertView
					.findViewById(R.id.merchant_item_address_distance);
			mHolder.mCurrencyText = (TextView) convertView.findViewById(R.id.commodity_item_per_currency);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		if (mList.get(position).get("iscurrency") != null) {
			if(mList.get(position).get("iscurrency").toString().equals("0")){
				mHolder.mCurrencyText.setText("消费币：不支持");
			}else if(mList.get(position).get("iscurrency").toString().equals("1")){
				mHolder.mCurrencyText.setText("消费币：支持");
			}
		}
		if (mList.get(position).get("Merchantname") != null) {
			mHolder.mMerchantName.setText(mList.get(position)
					.get("Merchantname").toString());
		}
		if (mList.get(position).get("Iswifi") != null) {
			if (mList.get(position).get("Iswifi").toString().equals("1")) {
				mHolder.mWifiText.setText("WIFI：" + "免费提供");
			} else {
				mHolder.mWifiText.setText("WIFI：" + "暂不提供");
			}
		}
		if (mList.get(position).get("Address") != null) {
			mHolder.mAddressText.setText(mList.get(position).get("Address")
					.toString());
		}
		if (mList.get(position).get("money") != null) {
			mHolder.mMoneyText.setText("人均￥"
					+ mList.get(position).get("money").toString());
		}
		if (mList.get(position).get("Flnum") != null) {
			mHolder.mFlumText.setText("返利"
					+ mList.get(position).get("Flnum").toString() + "%");
		}
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
			} else {
				mHolder.mPlStarImage
						.setImageResource(R.drawable.evaluation_xingxing_png0);
			}
		}
		if (SystemUtils.getSystemVersion() >= SystemUtils.V4_0) {
			mHolder.mMerchantImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		if (mList.get(position).get("Logoimage") != null) {
			mBitmapUtils.display(mHolder.mMerchantImage, mList.get(position)
					.get("Logoimage").toString());
		}
		return convertView;
	}

	class ViewHolder {
		private TextView mMerchantName;
		private TextView mWifiText;
		private TextView mAddressText;
		// private TextView mSoldText;
		private ImageView mPlStarImage;
		private ImageView mMerchantImage;
		private TextView mMoneyText;
		private TextView mFlumText;
		private TextView mDistanceText;
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
