package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.BitmapUtilSingle;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.popupwindow.AuctionPayPopupWindow;
import com.zjxfood.popupwindow.AuctionSuccessPopupWindow;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAuctionOrderNoPayItemActivity extends AppActivity implements
		OnClickListener {

	private ImageView mBackImage;
	private HashMap<String, Object> mDefaulAddressList;
	private TextView mAddressName, mAddressPhone, mAddressInfo;
	private Bundle mBundle;
	private static String mName, mPrice, mImageUrl, mOrderId = "", ExtField1,
			ExtField2, ExtField3, ExtField4, ExtField5, ExtField6, mId;
	private TextView mNameText, mPriceText, mExtText1, mExtText2, mExtText3,
			mExtText4, mExtText5, mExtText6;
	private BitmapUtils mBitmapUtils;
	private ImageView mImageView;
	private String mAddressId;
	private String mFlag = "";
	private HashMap<String, Object> mOrderMap;
	private AuctionSuccessPopupWindow mSuccessPopupWindow;
//	private PopupWindow mPopupWindow;
//	private TextView mOkText, mCancelText;
	private TextView mYfText;
	private String ShipmentFee,orderId;
	private ArrayList<HashMap<String, Object>> mAddressList;
	private TextView mOrderText;
	private AuctionPayPopupWindow mPayPopupWindow;
	private Button mPayBtn;
	private String mAutionId;	
	private TextView mTitleText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auction_order_no_pay_value);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mBitmapUtils = BitmapUtilSingle
				.getBitmapInstance(getApplicationContext());
		mBundle = getIntent().getExtras();
		Log.i("auctionnopay", "================");
		if (mBundle != null) {
			mFlag = mBundle.getString("flag");
			if (mFlag.equals("1")) {
				mName = mBundle.getString("ProductName");
				mPrice = mBundle.getString("BasePrice");
				mImageUrl = mBundle.getString("ImgUrl");
				mOrderId = mBundle.getString("OrderId");
				ExtField1 = mBundle.getString("ExtField1");
				ExtField2 = mBundle.getString("ExtField2");
				ExtField3 = mBundle.getString("ExtField3");
				ExtField4 = mBundle.getString("ExtField4");
				ExtField5 = mBundle.getString("ExtField5");
				ExtField6 = mBundle.getString("ExtField6");
				mAutionId = mBundle.getString("AutionId");
				Log.i("sdasdsad", mOrderId+"===="+ExtField2+"==="+ExtField3);
				mId = mBundle.getString("Id");
				mAddressId = mBundle.getString("AddressId");
				ShipmentFee = mBundle.getString("ShipmentFee");
				getAddress();
			} else if (mFlag.equals("2")) {
				mAddressId = mBundle.getString("addressId");
				mAddressName.setText("收货人姓名：" + mBundle.getString("userName"));
				mAddressPhone
						.setText("收货人电话：" + mBundle.getString("userPhone"));
				mAddressInfo.setText("收货人地址："
						+ mBundle.getString("userAddress"));
				Log.i("addressId", mAddressId + "=============");
			}
			setResource();
		}
	}

	private void init() {
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("拍卖订单");
		mAddressName = (TextView) findViewById(R.id.auction_value_receiving_name);
		mAddressPhone = (TextView) findViewById(R.id.auction_value_receiving_phone);
		mAddressInfo = (TextView) findViewById(R.id.auction_value_receiving_address);
		mNameText = (TextView) findViewById(R.id.auction_value_head_name);
		mPriceText = (TextView) findViewById(R.id.auction_value_head_price_value);
		mExtText1 = (TextView) findViewById(R.id.auction_value_order_info1);
		mExtText2 = (TextView) findViewById(R.id.auction_value_order_info2);
		mExtText3 = (TextView) findViewById(R.id.auction_value_order_info3);
		mExtText4 = (TextView) findViewById(R.id.auction_value_order_info4);
		mExtText5 = (TextView) findViewById(R.id.auction_value_order_info5);
		mExtText6 = (TextView) findViewById(R.id.auction_value_order_info6);
		mImageView = (ImageView) findViewById(R.id.auction_value_image);
		mYfText = (TextView) findViewById(R.id.auction_value_head_yf);
		mOrderText = (TextView) findViewById(R.id.auction_value_order_order);
		mPayBtn = (Button) findViewById(R.id.auction_order_value_pay_btn);

		mPayBtn.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	private void setResource() {
		mNameText.setText(mName);
		mPriceText.setText(mPrice);
		mExtText1.setText(ExtField1);
		mExtText2.setText(ExtField2);
		mExtText3.setText(ExtField3);
		mExtText4.setText(ExtField4);
		mExtText5.setText(ExtField5);
		mExtText6.setText(ExtField6);
		mBitmapUtils.display(mImageView, mImageUrl);
		mOrderText.setText("订单号："+mOrderId);
		mYfText.setText("快递费：￥"+ShipmentFee);
	}

	OnItemClickListener mItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
		}
	};

	// 获取收货地址
//	private void getAddress() {
//		RequestParams params = new RequestParams();
//		params.put("addressid", mAddressId);
//		AsyUtils.get(Constants.getAddressById, params,
//				new AsyncHttpResponseHandler() {
//					@Override
//					@Deprecated
//					public void onSuccess(int statusCode, String content) {
//						try {
//							mAddressList = Constants
//									.getJsonArrayByData(content);
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						if (mAddressList != null && mAddressList.size() > 0) {
//							handler.sendEmptyMessageDelayed(3, 0);
//						}
//						super.onSuccess(statusCode, content);
//					}
//				});
//	}
	private void getAddress() {
		StringBuilder sb = new StringBuilder();
			sb.append("addressid=" + mAddressId);
		XutilsUtils.get(Constants.getAddressById, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						try {
							mAddressList = Constants
									.getJsonArrayByData(res.result);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (mAddressList != null && mAddressList.size() > 0) {
							handler.sendEmptyMessageDelayed(3, 0);
						}
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (!Constants.isNull(mDefaulAddressList.get("Address"))) {
					mAddressInfo.setText("收货人地址："
							+ mDefaulAddressList.get("Address"));
				}
				if (!Constants.isNull(mDefaulAddressList.get("Mobile"))) {
					mAddressPhone.setText("收货人电话："
							+ mDefaulAddressList.get("Mobile"));
				}
				if (!Constants.isNull(mDefaulAddressList.get("Realname"))) {
					mAddressName.setText("收货人姓名："
							+ mDefaulAddressList.get("Address"));
				}
				if (!Constants.isNull(mDefaulAddressList.get("Id"))) {
					mAddressId = mDefaulAddressList.get("Id").toString();
				}
				Log.i("addressId", mAddressId + "=============");
				break;

			case 2:
				if (mOrderMap.get("Code") != null
						&& mOrderMap.get("Code").toString().equals("200")) {
					mSuccessPopupWindow = new AuctionSuccessPopupWindow(
							MyAuctionOrderNoPayItemActivity.this, "付款成功");
					mSuccessPopupWindow.showAtLocation(mAddressName,
							Gravity.CENTER, 0, 0);
				} else {
					if (mOrderMap.get("Message") != null) {
						mSuccessPopupWindow = new AuctionSuccessPopupWindow(
								MyAuctionOrderNoPayItemActivity.this, mOrderMap
										.get("Message").toString());
						mSuccessPopupWindow.showAtLocation(mAddressName,
								Gravity.CENTER, 0, 0);
						// Toast.makeText(getApplicationContext(),
						// mOrderMap.get("Message").toString(),
						// Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case 3:
				if(!Constants.isNull(mAddressList.get(0).get("address"))){
					mAddressInfo.setText("收货人地址："+mAddressList.get(0).get("address"));
				}
				if(!Constants.isNull(mAddressList.get(0).get("realname"))){
					mAddressName.setText("收货人姓名："+mAddressList.get(0).get("realname"));
				}
				if(!Constants.isNull(mAddressList.get(0).get("mobile"))){
					mAddressPhone.setText("收货人电话："+mAddressList.get(0).get("mobile"));
				}
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		//付款
		case R.id.auction_order_value_pay_btn:
			mPayPopupWindow = new AuctionPayPopupWindow(MyAuctionOrderNoPayItemActivity.this, mClickListener, ShipmentFee);
			mPayPopupWindow.showAtLocation(mAddressName, Gravity.CENTER, 0,0);
			break;
		case R.id.title_back_image:
			finish();
			break;
		}
	}
	OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			switch (v.getId()) {
			case R.id.auction_to_pay_ok_true:
				if(ShipmentFee!=null && !ShipmentFee.equals("")){
				// 跳转到支付方式
					intent.setClass(getApplicationContext(),
							MallPayWayActivity.class);
					bundle = new Bundle();
					bundle.putString("price",Float.parseFloat(ShipmentFee)+"");
					bundle.putString("merchantName", mName);
					bundle.putString("mId", mAutionId);
					bundle.putString("type", "gift");
					bundle.putString("flum", "0");
					bundle.putString("LoginImage", mImageUrl);
					bundle.putString("mallOrMerchant", "auction");
					bundle.putString("address", mAddressId);
					bundle.putString("payType", "auctionYf");
					bundle.putString("giftId", mAutionId);
					bundle.putString("orderId", mOrderId);
					bundle.putString("sizeId", "");
					intent.putExtras(bundle);
					startActivity(intent);
					mPayPopupWindow.dismiss();
				}
				break;
			}
		}
	};

//	private void createOrder() {
//		RequestParams params = new RequestParams();
//		params.put("auctionId", mId);
//		params.put("userId", Constants.mId);
//		params.put("addressId", mAddressId);
//		AsyUtils.get(Constants.createAuctionOrder, params,
//				new AsyncHttpResponseHandler() {
//					@Override
//					@Deprecated
//					public void onSuccess(int statusCode, String content) {
//						mOrderMap = Constants.getJsonObject(content);
//						if (mOrderMap != null && mOrderMap.size() > 0) {
//							handler.sendEmptyMessageDelayed(2, 0);
//						}
//						super.onSuccess(statusCode, content);
//					}
//				});
//	}
	private void createOrder() {
		StringBuilder sb = new StringBuilder();
			sb.append("auctionId=" + mId+"&userId="+Constants.mId+"&addressId="+mAddressId);
		XutilsUtils.get(Constants.createAuctionOrder, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mOrderMap = Constants.getJsonObject(res.result);
						if (mOrderMap != null && mOrderMap.size() > 0) {
							handler.sendEmptyMessageDelayed(2, 0);
						}
					}
				});
	}
}
