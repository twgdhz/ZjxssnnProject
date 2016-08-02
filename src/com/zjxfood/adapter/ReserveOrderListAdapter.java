package com.zjxfood.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;
import com.zjxfood.interfaces.MyInterfaceImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReserveOrderListAdapter extends BaseAdapter {

	private Context mContext;
	private HashMap<String, ArrayList<HashMap<String, Object>>> mMap;
	private ArrayList<HashMap<String, Object>> mList;
	private MyInterfaceImpl mClick;
	private ArrayList<HashMap<String, Object>> mArrayList;
	private BitmapUtils mBitmapUtils;
	private float allPrice = 0;
	public ReserveOrderListAdapter(Context context, MyInterfaceImpl myClick,
			ArrayList<HashMap<String, Object>> arrayList) {
		this.mContext = context;
		this.mClick = myClick;
		this.mArrayList = arrayList;
		mBitmapUtils = BitmapUtilSingle.getBitmapInstance(mContext);
		mBitmapUtils.configDefaultLoadFailedImage(R.drawable.dishes_failure);
	}

	@Override
	public int getCount() {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		final ViewHolder mHolder;
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.reserve_order_list_item,
					null);
			mHolder.mTextView = (TextView) convertView
					.findViewById(R.id.reserve_item_jian_add_image);
			mHolder.mAddImage = (ImageView) convertView
					.findViewById(R.id.reserve_item_add_image);
			mHolder.mJianImage = (ImageView) convertView
					.findViewById(R.id.reserve_item_jian_image);
			mHolder.mNameText = (TextView) convertView
					.findViewById(R.id.reserve_order_item_name);
			mHolder.mPriceText = (TextView) convertView
					.findViewById(R.id.reserve_order_item_price);
			mHolder.mSaleText = (TextView) convertView
					.findViewById(R.id.reserve_order_item_sold);
			mHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.reserve_item_image);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if (!Constants.isNull(mArrayList.get(position).get("name"))) {
			mHolder.mNameText.setText(mArrayList.get(position).get("name")
					.toString());
		}
		if (!Constants.isNull(mArrayList.get(position).get("price"))) {
			mHolder.mPriceText.setText("￥"+mArrayList.get(position).get("price")
					.toString());
		}
		if (!Constants.isNull(mArrayList.get(position).get("salecount"))) {
			mHolder.mSaleText.setText("已售："+mArrayList.get(position).get("salecount")
					.toString());
		}
		if (!Constants.isNull(mArrayList.get(position).get("image"))) {
			mBitmapUtils.display(mHolder.mImageView, mArrayList.get(position)
					.get("image").toString());
//			mBitmapUtils.configDefaultLoadingImage(bitmap)
			//
		}
		mHolder.mAddImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mMap == null) {
					mMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				}
				mList = mMap.get(position + "");
				if (mList == null) {
					mList = new ArrayList<HashMap<String, Object>>();
				}
				allPrice = Float.parseFloat(mArrayList.get(position).get("price").toString())+allPrice;
				mList.add(mArrayList.get(position));
				mMap.put(position + "", mList);
			
				Log.i("position", position + "===============");
				mHolder.mJianImage.setImageResource(R.drawable.reduce);
				if (mHolder.mTextView.getText().toString().equals("")) {
					mHolder.mTextView.setText("1");
				} else {
					mHolder.mTextView.setText(Integer
							.parseInt(mHolder.mTextView.getText().toString())
							+ 1 + "");
				}
				mClick.doUpdateOrder(mMap,allPrice);
			}
		});
		mHolder.mJianImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mMap == null) {
					mMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
				}
				mList = mMap.get(position + "");
				if (mList == null) {
					mList = new ArrayList<HashMap<String, Object>>();
				}
				allPrice = allPrice-Float.parseFloat(mArrayList.get(position).get("price").toString());
				if (mList.size() > 1) {
					mList.remove(0);
					mMap.put("" + position, mList);
				} else {
					Log.i("remove", mMap.size() + "==========");
					mMap.remove(position + "");
				}
				Log.i("position", position + "===============");
				if (mHolder.mTextView.getText().toString().equals("")) {
					// mHolder.mTextView.setText("1");
				} else {
					mHolder.mTextView.setText(Integer
							.parseInt(mHolder.mTextView.getText().toString())
							- 1 + "");
				}
				if (mHolder.mTextView.getText().toString().equals("0")) {
					mHolder.mTextView.setText("");
					mHolder.mJianImage.setImageDrawable(null);
				}
				mClick.doUpdateOrder(mMap,allPrice);
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView mTextView;
		ImageView mAddImage;
		ImageView mJianImage;
		TextView mNameText;
		TextView mPriceText;
		ImageView mImageView;
		TextView mSaleText;
	}
}
