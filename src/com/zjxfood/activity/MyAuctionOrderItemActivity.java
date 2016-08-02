package com.zjxfood.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.util.BitmapUtilSingle;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.popupwindow.AuctionSuccessPopupWindow;

import org.json.JSONObject;

import java.util.HashMap;

public class MyAuctionOrderItemActivity extends AppActivity implements
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
	private Button mPayButton;
	private String mAddressId;
	private Button mModifyAddressBtn;
	private String mFlag = "";
	private HashMap<String, Object> mOrderMap;
	private AuctionSuccessPopupWindow mSuccessPopupWindow;
	private PopupWindow mPopupWindow;
	private TextView mOkText, mCancelText;
	private TextView mYfText;
	private String st = "";
	private String ShipmentFee;
	private TextView mTitleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auction_order_value);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		MallDetailActivity.mFlag = "2";
		mBitmapUtils = BitmapUtilSingle
				.getBitmapInstance(getApplicationContext());
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			mFlag = mBundle.getString("flag");
			if (mFlag.equals("1")) {
				st = mBundle.getString("st");
				mName = mBundle.getString("ProductName");
				mPrice = mBundle.getString("BasePrice");
				mImageUrl = mBundle.getString("ImgUrl");
				mOrderId = mBundle.getString("mOrderId");
				ExtField1 = mBundle.getString("ExtField1");
				ExtField2 = mBundle.getString("ExtField2");
				ExtField3 = mBundle.getString("ExtField3");
				ExtField4 = mBundle.getString("ExtField4");
				ExtField5 = mBundle.getString("ExtField5");
				ExtField6 = mBundle.getString("ExtField6");
				ShipmentFee = mBundle.getString("ShipmentFee");
				Log.i("ShipmentFee", ShipmentFee+"===========需要付款========");
				mId = mBundle.getString("Id");
					mPayButton.setText("创建订单");
				
				if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
					// 获取默认地址
					getDefaultAddress();
				}
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
		mPayButton = (Button) findViewById(R.id.auction_order_value_btn);
		mModifyAddressBtn = (Button) findViewById(R.id.auction_order_value_modity_btn);
		mYfText = (TextView) findViewById(R.id.auction_value_head_yf);

		mBackImage.setOnClickListener(this);
		mPayButton.setOnClickListener(this);
		mModifyAddressBtn.setOnClickListener(this);
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
	}

	OnItemClickListener mItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
		}
	};

	// 获取默认地址
	private void getDefaultAddress() {
		RequestParams params = new RequestParams();
		params.put("uid", Constants.mId);
		AsyUtils.get(Constants.getDefaultAddress2, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						try {
							mDefaulAddressList = Constants
									.getJsonObject(response.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (mDefaulAddressList != null
								&& mDefaulAddressList.size() > 0) {
							handler.sendEmptyMessageDelayed(1, 0);
						}
						super.onSuccess(response);
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
							MyAuctionOrderItemActivity.this, "订单创建成功");
					mSuccessPopupWindow.showAtLocation(mAddressName,
							Gravity.CENTER, 0, 0);
					if(ShipmentFee!=null && !ShipmentFee.equals("")){
						// 跳转到支付方式
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
							intent.setClass(getApplicationContext(),
									MallPayWayActivity.class);
							bundle = new Bundle();
							bundle.putString("price",mPrice+"");
							bundle.putString("merchantName", mName);
							bundle.putString("mId", mId);
							bundle.putString("type", "gift");
							bundle.putString("flum", "0");
							bundle.putString("LoginImage", mImageUrl);
							bundle.putString("mallOrMerchant", "auction");
							bundle.putString("address", mAddressId);
							bundle.putString("giftId", mId);
							bundle.putString("payType", "auctionYf");
							bundle.putString("orderId", mOrderId);
							bundle.putString("sizeId", "");
							intent.putExtras(bundle);
							startActivity(intent);
						}
				} else {
					if (mOrderMap.get("Message") != null) {
						mSuccessPopupWindow = new AuctionSuccessPopupWindow(
								MyAuctionOrderItemActivity.this, mOrderMap.get("Message").toString());
						mSuccessPopupWindow.showAtLocation(mAddressName,
								Gravity.CENTER, 0, 0);
					}
				}
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.title_back_image:
			finish();
			break;
		// 付款
		case R.id.auction_order_value_btn:
			LayoutInflater inflater = LayoutInflater
					.from(getApplicationContext());
			View view = inflater.inflate(R.layout.auction_value_popup, null);
			mOkText = (TextView) view.findViewById(R.id.auction_pay_ok_true);
			mCancelText = (TextView) view
					.findViewById(R.id.auction_pay_cancel_false);
			mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, false);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.setFocusable(true);
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			// 设置SelectPicPopupWindow弹出窗体的背景
			mPopupWindow.setBackgroundDrawable(dw);
			// 设置SelectPicPopupWindow弹出窗体动画效果
			mPopupWindow.setAnimationStyle(R.style.AnimTop_miss);
			mPopupWindow.showAtLocation(mAddressName, Gravity.CENTER, 0, 0);
			mOkText.setOnClickListener(mClickListener);
			mCancelText.setOnClickListener(mClickListener);
			break;
		// 更好地址
		case R.id.auction_order_value_modity_btn:
			intent.setClass(getApplicationContext(),
					NewAddressManageActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("flag", "2");
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			break;
		}
	}

	OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.auction_pay_ok_true:
				mPopupWindow.dismiss();
				createOrder();
				break;

			case R.id.auction_pay_cancel_false:
				mPopupWindow.dismiss();
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
	//创建订单
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
